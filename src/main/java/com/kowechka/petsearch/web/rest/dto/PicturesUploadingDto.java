package com.kowechka.petsearch.web.rest.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicturesUploadingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer processedPictures;
    private Integer numberOfPictures;
    private List<PictureDto> result;
}
