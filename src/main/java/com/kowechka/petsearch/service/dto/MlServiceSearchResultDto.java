package com.kowechka.petsearch.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MlServiceSearchResultDto {

    String downloadUrl;
    String dateOfShoot;
    String latLong;
}
