package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.Dog;
import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.domain.User;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import com.kowechka.petsearch.repository.PetSearchEntityRepository;
import com.kowechka.petsearch.service.*;
import com.kowechka.petsearch.service.exception.CantGetCurrentUserException;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import java.util.Optional;
import java.util.stream.Collectors;
import liquibase.pro.packaged.P;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PetSearchServiceImpl implements PetSearchService {

    private final Logger log = LoggerFactory.getLogger(PetSearchServiceImpl.class);

    private final FileStorageService fileStorageService;
    private final PetSearchEntityRepository petSearchEntityRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final MlPictureClassifierService mlPictureClassifierService;

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

        PetSearchEntity withId = save(pending);

        //TODO: refactor to async uploading
        withId.setPictures(
            dto
                .getFilesToUpload()
                .stream()
                .map(file ->
                    pictureService.save(
                        Picture.builder().search(withId).filePath(fileStorageService.upload(file)).user(currentUser).build()
                    )
                )
                .collect(Collectors.toSet())
        );

        // aka async call
        mlPictureClassifierService.sendSearchToClassification(withId);

        return save(withId);
    }

    @Override
    public PetSearchEntity save(PetSearchEntity petSearchEntity) {
        log.debug("Request to save PetSearchEntity : {}", petSearchEntity);

        return petSearchEntityRepository.save(petSearchEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetSearchEntity> findOne(Long id) {
        log.debug("Request to get PetSearchEntity with id : {}", id);

        return petSearchEntityRepository.findById(id);
    }
}
