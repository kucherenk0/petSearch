package com.kowechka.petsearch.web.rest.dto;

import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetSearchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate dateOfLost;
    private String address;
    private List<String> picturesDownloadUrls;
    private SearchStatus status;
}
