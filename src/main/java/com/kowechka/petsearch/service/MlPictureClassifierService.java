package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PetSearchEntity;
import java.util.List;

public interface MlPictureClassifierService {
    void sendSearchToClassification(PetSearchEntity search);

    Boolean isCompleted(PetSearchEntity search);

    List<String> getClassificationResult(PetSearchEntity search);
}
