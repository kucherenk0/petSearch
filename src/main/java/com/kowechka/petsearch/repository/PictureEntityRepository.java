package com.kowechka.petsearch.repository;

import com.kowechka.petsearch.domain.PictureEntity;
import java.util.List;
import liquibase.pro.packaged.L;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureEntityRepository extends JpaRepository<PictureEntity, Long> {
    List<PictureEntity> findAllByPicturesUploadingId(Long id);
}
