package com.kowechka.petsearch.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kowechka.petsearch.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PetSearchEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetSearchEntity.class);
        PetSearchEntity petSearchEntity1 = new PetSearchEntity();
        petSearchEntity1.setId(1L);
        PetSearchEntity petSearchEntity2 = new PetSearchEntity();
        petSearchEntity2.setId(petSearchEntity1.getId());
        assertThat(petSearchEntity1).isEqualTo(petSearchEntity2);
        petSearchEntity2.setId(2L);
        assertThat(petSearchEntity1).isNotEqualTo(petSearchEntity2);
        petSearchEntity1.setId(null);
        assertThat(petSearchEntity1).isNotEqualTo(petSearchEntity2);
    }
}
