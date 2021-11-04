package com.kowechka.petsearch.repository;

import com.kowechka.petsearch.domain.PicturesUploading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PicturesUploading entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PicturesUploadingRepository
    extends JpaRepository<PicturesUploading, Long> {}
