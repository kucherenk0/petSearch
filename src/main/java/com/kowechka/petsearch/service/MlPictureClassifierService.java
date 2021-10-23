package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PetSearchEntity;

public interface MlPictureClassifierService {
    void sendSearchToClassification(PetSearchEntity search);

    Boolean isCompleted(PetSearchEntity search);
}
