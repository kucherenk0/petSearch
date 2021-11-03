package com.kowechka.petsearch.service.mapper;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PetSearchDtoMapper {

    public PetSearchDto toDto(PetSearchEntity entity) {
        return PetSearchDto
            .builder()
            .id(entity.getId())
            .color(entity.getColor())
            .radius(entity.getRadius())
            .tail(entity.getTail())
            .status(entity.getStatus())
            .dateOfLost(entity.getDateOfLost())
            .address(entity.getAddress())
            .build();
    }
}
