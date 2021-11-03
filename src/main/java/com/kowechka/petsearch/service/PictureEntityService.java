package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PictureEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PictureEntityService {
    PictureEntity save(PictureEntity pictureEntity);

    Optional<PictureEntity> partialUpdate(PictureEntity pictureEntity);

    Page<PictureEntity> findAll(Pageable pageable);

    Optional<PictureEntity> findOne(Long id);

    void delete(Long id);
}
