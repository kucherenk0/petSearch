package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.domain.PictureEntity;
import com.kowechka.petsearch.domain.User;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import com.kowechka.petsearch.repository.PetSearchEntityRepository;
import com.kowechka.petsearch.service.*;
import com.kowechka.petsearch.service.client.MlServiceClient;
import com.kowechka.petsearch.service.dto.MlServiceSearchResultDto;
import com.kowechka.petsearch.service.exception.CantGetCurrentUserException;
import com.kowechka.petsearch.service.mapper.PetSearchDtoMapper;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PetSearchServiceImpl implements PetSearchService {

    private final Logger log = LoggerFactory.getLogger(PetSearchServiceImpl.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy-HH:mm:ss"
    );

    private final PetSearchEntityRepository petSearchEntityRepository;
    private final UserService userService;
    private final MlServiceClient mlServiceClient;
    private final PetSearchDtoMapper petSearchDtoMapper;
    private final PictureEntityService pictureEntityService;

    @Override
    public PetSearchDto createEntityAndRunSearch(CreatePetSearchDto dto) {
        log.debug("Request to create new Search with PetSearchEntity : {}", dto);

        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(CantGetCurrentUserException::new);

        PetSearchEntity pending = PetSearchEntity
            .builder()
            .status(SearchStatus.PENDING)
            .dateOfLost(dto.getDateOfLost())
            .address(dto.getAddress())
            .color(dto.getColor())
            .radius(dto.getRadius())
            .tail(dto.getTail())
            .user(currentUser)
            .build();

        PetSearchEntity saved = save(pending);

        List<Long> resultPictureIds = mlServiceClient.getSearchResult(saved);

        saved.setStatus(SearchStatus.DONE);
        save(saved);

        PetSearchDto result = petSearchDtoMapper.toDto(saved);

        result.setResult(
            resultPictureIds
                .stream()
                .map(this::getMlServiceSearchResultDto)
                .collect(Collectors.toList())
        );

        return result;
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
        return requestedSearch.map(petSearchDtoMapper::toDto);
    }

    private MlServiceSearchResultDto getMlServiceSearchResultDto(Long id) {
        PictureEntity picture = pictureEntityService
            .findOne(id)
            .orElseThrow(() ->
                new RuntimeException("Cant' find picture with id " + id.toString())
            );

        return MlServiceSearchResultDto
            .builder()
            .dateOfShoot(picture.getDate().format(formatter))
            .downloadUrl(picture.getDownloadUrl())
            .latLong(picture.getLat() + ", " + picture.getLon())
            .build();
    }
}
