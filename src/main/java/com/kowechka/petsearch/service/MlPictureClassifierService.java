package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.service.dto.MlServiceSearchResultDto;
import java.util.List;

public interface MlPictureClassifierService {
    void sendSearchToClassification(PetSearchEntity search);

    void sendPictureToClassification(String path);

    Boolean isCompleted(PetSearchEntity search);

    List<MlServiceSearchResultDto> getClassificationResult(PetSearchEntity search);

    List<MlServiceSearchResultDto> getClassificationResultForPictures(
        PetSearchEntity search
    );
}
