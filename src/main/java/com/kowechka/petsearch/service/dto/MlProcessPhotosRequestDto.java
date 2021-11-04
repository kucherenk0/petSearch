package com.kowechka.petsearch.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MlProcessPhotosRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Long> pictureIds = new ArrayList<>();
}
