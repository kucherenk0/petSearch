package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.Picture;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Picture}.
 */
public interface PictureService {
    /**
     * Save a picture.
     *
     * @param picture the entity to save.
     * @return the persisted entity.
     */
    Picture save(Picture picture);

    /**
     * Partially updates a picture.
     *
     * @param picture the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Picture> partialUpdate(Picture picture);

    /**
     * Get all the pictures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Picture> findAll(Pageable pageable);

    /**
     * Get the "id" picture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Picture> findOne(Long id);

    /**
     * Delete the "id" picture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
