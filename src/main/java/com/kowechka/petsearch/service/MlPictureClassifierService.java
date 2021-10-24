package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.service.dto.ClassificationResultDto;
import java.util.List;

public interface MlPictureClassifierService {
    void sendSearchToClassification(PetSearchEntity search);

    Boolean isCompleted(PetSearchEntity search);

    List<ClassificationResultDto> getClassificationResult(PetSearchEntity search);
}
