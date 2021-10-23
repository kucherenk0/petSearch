package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.domain.User;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import com.kowechka.petsearch.repository.PetSearchEntityRepository;
import com.kowechka.petsearch.service.*;
import com.kowechka.petsearch.service.exception.CantGetCurrentUserException;
import com.kowechka.petsearch.service.mapper.PetSearchDtoMapper;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.web.util.ResponseUtil;

@Service
@AllArgsConstructor
public class PetSearchServiceImpl implements PetSearchService {

    private final Logger log = LoggerFactory.getLogger(PetSearchServiceImpl.class);

    public static final String USER_UPLOAD_EXTERNAL_ID = "user_upload";

    private final FileStorageService fileStorageService;
    private final PetSearchEntityRepository petSearchEntityRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final MlPictureClassifierService mlPictureClassifierService;
    private final PetSearchDtoMapper petSearchDtoMapper;

    @Override
    public PetSearchEntity createEntityAndRunSearch(CreatePetSearchDto dto) {
        log.debug("Request to create new Search with PetSearchEntity : {}", dto);

        User currentUser = userService.getUserWithAuthorities().orElseThrow(CantGetCurrentUserException::new);

        PetSearchEntity pending = PetSearchEntity
            .builder()
            .status(SearchStatus.PENDING)
            .dateOfLost(dto.getDateOfLost())
            .adderss(dto.getAddress())
            .user(currentUser)
            .build();

        PetSearchEntity saved = save(pending);

        //TODO: refactor to async uploading

        var uploadedPictures = dto
            .getFilesToUpload()
            .stream()
            .map(file ->
                pictureService.save(
                    Picture
                        .builder()
                        .petSearchId(saved.getId())
                        .externalId(USER_UPLOAD_EXTERNAL_ID)
                        .filePath(fileStorageService.upload(file))
                        .user(currentUser)
                        .build()
                )
            )
            .collect(Collectors.toSet());

        saved.setPictures(uploadedPictures);

        // aka async call
        mlPictureClassifierService.sendSearchToClassification(saved);

        saved.setStatus(SearchStatus.IN_PROGRESS);

        return save(saved);
    }

    @Transactional
    @Override
    public PetSearchEntity save(PetSearchEntity petSearchEntity) {
        log.debug("Request to save PetSearchEntity : {}", petSearchEntity);

        return petSearchEntityRepository.save(petSearchEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetSearchDto> findOne(Long id) {
        log.debug("Request to get PetSearchEntity with id : {}", id);

        var requestedSearch = petSearchEntityRepository.findById(id);
        if (requestedSearch.isPresent()) {
            var presentSearch = requestedSearch.get();
            if (mlPictureClassifierService.isCompleted(presentSearch)) {
                presentSearch.setStatus(SearchStatus.DONE);
                var dto = petSearchDtoMapper.toDto(presentSearch);
                dto.setPicturesDownloadUrls(mlPictureClassifierService.getClassificationResult(presentSearch));
                return Optional.of(dto);
            } else {
                return Optional.of(petSearchDtoMapper.toDto(requestedSearch.get()));
            }
        }

        return Optional.empty();
    }
}
