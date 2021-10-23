package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import java.util.Optional;

public interface PetSearchService {
    PetSearchEntity createEntityAndRunSearch(CreatePetSearchDto dto);

    PetSearchEntity save(PetSearchEntity petSearchEntity);

    Optional<PetSearchEntity> findOne(Long id);
}
