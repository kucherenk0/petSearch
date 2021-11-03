package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.PictureEntity;
import com.kowechka.petsearch.repository.PictureEntityRepository;
import com.kowechka.petsearch.service.PictureEntityService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PictureEntityServiceImpl implements PictureEntityService {

    private final Logger log = LoggerFactory.getLogger(PictureEntityServiceImpl.class);

    private final PictureEntityRepository pictureEntityRepository;

    @Override
    public PictureEntity save(PictureEntity picture) {
        log.debug("Request to save PictureEntity : {}", picture);
        return pictureEntityRepository.save(picture);
    }

    @Override
    public Optional<PictureEntity> partialUpdate(PictureEntity picture) {
        log.debug("Request to partially update PictureEntity : {}", picture);

        return pictureEntityRepository
            .findById(picture.getId())
            .map(existingPicture -> {
                if (picture.getDownloadUrl() != null) {
                    existingPicture.setDownloadUrl(picture.getDownloadUrl());
                }
                if (picture.getHasDog() != null) {
                    existingPicture.setHasDog(picture.getHasDog());
                }
                if (picture.getHasAnimal() != null) {
                    existingPicture.setHasAnimal(picture.getHasAnimal());
                }
                if (picture.getHasOwner() != null) {
                    existingPicture.setHasOwner(picture.getHasOwner());
                }
                if (picture.getAddress() != null) {
                    existingPicture.setAddress(picture.getAddress());
                }
                if (picture.getLat() != null) {
                    existingPicture.setLat(picture.getLat());
                }
                if (picture.getLon() != null) {
                    existingPicture.setLon(picture.getLon());
                }
                if (picture.getCameraUid() != null) {
                    existingPicture.setCameraUid(picture.getCameraUid());
                }
                if (picture.getDate() != null) {
                    existingPicture.setDate(picture.getDate());
                }
                return existingPicture;
            })
            .map(pictureEntityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PictureEntity> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return pictureEntityRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PictureEntity> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureEntityRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureEntityRepository.deleteById(id);
    }
}
