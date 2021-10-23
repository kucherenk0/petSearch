package com.kowechka.petsearch.repository;

import com.kowechka.petsearch.domain.PetSearchEntity;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PetSearchEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetSearchEntityRepository extends JpaRepository<PetSearchEntity, Long> {
    @Query("select petSearchEntity from PetSearchEntity petSearchEntity where petSearchEntity.user.login = ?#{principal.username}")
    List<PetSearchEntity> findByUserIsCurrentUser();
}
