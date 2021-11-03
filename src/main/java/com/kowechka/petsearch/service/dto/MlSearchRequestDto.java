package com.kowechka.petsearch.service.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MlSearchRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    String dateOfLost;
    String address;
    Integer color;
    Integer tail;
    Double radius;
}
