package com.kowechka.petsearch.web.rest.dto;

import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import com.kowechka.petsearch.service.dto.MlServiceSearchResultDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetSearchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime dateOfLost;
    private String address;
    private List<MlServiceSearchResultDto> result;
    private SearchStatus status;
    private int color;
    private int tail;
    private int radius;
}
