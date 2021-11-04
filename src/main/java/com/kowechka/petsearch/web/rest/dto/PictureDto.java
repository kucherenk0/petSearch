package com.kowechka.petsearch.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PictureDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String downloadUrl;
    private Boolean hasDog;
    private Boolean hasAnimal;
    private Boolean hasOwner;
    private Integer color;
    private Integer tail;
    private String address;
    private String lat;
    private String lon;
    private String cameraUid;
    private LocalDateTime date;
}
