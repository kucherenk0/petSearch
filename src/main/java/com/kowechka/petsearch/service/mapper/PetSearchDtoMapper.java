package com.kowechka.petsearch.service.mapper;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PetSearchDtoMapper {

    //  @ConfigurationPropertiesBinding
    public static final String serverPort = "8080";

    public static final String PICTURE_CONTENT_URL = "api/pictures/content";
    public static final String CURRENT_HOST_NAME = "localhost:" + serverPort;

    public PetSearchDto toDto(PetSearchEntity entity) {
        return PetSearchDto
            .builder()
            .id(entity.getId())
            .status(entity.getStatus())
            .dateOfLost(entity.getDateOfLost())
            .address(entity.getAdderss())
            .build();
    }

    private String getDownloadUrlForPicture(Picture picture) {
        return CURRENT_HOST_NAME + PICTURE_CONTENT_URL + picture.getId();
    }
}
