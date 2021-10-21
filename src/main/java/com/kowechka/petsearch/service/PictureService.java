package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.domain.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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

    Picture savePictureAndStoreContent(User user, MultipartFile pictureFile) throws IOException;

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

    List<Picture> findAllBySearchId(User user, Long searchId);

    InputStream getPictureContentStreamById(Long id);

    /**
     * Delete the "id" picture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
