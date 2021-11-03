package com.kowechka.petsearch.service.client;

import com.kowechka.petsearch.domain.PetSearchEntity;
import java.util.List;

public interface MlServiceClient {
    List<Long> getSearchResult(PetSearchEntity search);
}
