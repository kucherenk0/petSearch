package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.repository.PictureRepository;
import com.kowechka.petsearch.service.PictureService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Picture}.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final PictureRepository pictureRepository;

    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture save(Picture picture) {
        log.debug("Request to save Picture : {}", picture);
        return pictureRepository.save(picture);
    }

    @Override
    public Optional<Picture> partialUpdate(Picture picture) {
        log.debug("Request to partially update Picture : {}", picture);

        return pictureRepository
            .findById(picture.getId())
            .map(existingPicture -> {
                if (picture.getExternalId() != null) {
                    existingPicture.setExternalId(picture.getExternalId());
                }
                if (picture.getHasDog() != null) {
                    existingPicture.setHasDog(picture.getHasDog());
                }
                if (picture.getFilePath() != null) {
                    existingPicture.setFilePath(picture.getFilePath());
                }
                if (picture.getStreetAddress() != null) {
                    existingPicture.setStreetAddress(picture.getStreetAddress());
                }
                if (picture.getCameraId() != null) {
                    existingPicture.setCameraId(picture.getCameraId());
                }
                if (picture.getDateOfShoot() != null) {
                    existingPicture.setDateOfShoot(picture.getDateOfShoot());
                }

                return existingPicture;
            })
            .map(pictureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Picture> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Picture> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
    }
}
