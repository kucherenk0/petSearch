package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import java.util.Optional;

public interface PetSearchService {
    PetSearchEntity createEntityAndRunSearch(CreatePetSearchDto dto);

    PetSearchEntity save(PetSearchEntity petSearchEntity);

    Optional<PetSearchDto> findOne(Long id);
}
