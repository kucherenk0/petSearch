package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.domain.User;
import com.kowechka.petsearch.repository.PictureRepository;
import com.kowechka.petsearch.service.PictureService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Picture}.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    public static final String FILE_STORAGE_FOLDER_PATH = "file-storage/";

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
    public Picture savePictureAndStoreContent(User user, MultipartFile pictureFile) throws IOException {
        byte[] bytes = pictureFile.getBytes();
        //String newFileName = RandomStringUtils.randomAlphanumeric(8);
        String newFileName = RandomStringUtils.randomAlphanumeric(3) + pictureFile.getOriginalFilename();
        Path path = Path.of(FILE_STORAGE_FOLDER_PATH + newFileName).toAbsolutePath();
        Files.write(path, bytes);

        Picture toSave = new Picture();
        toSave.externalId("user_upload");
        toSave.setFilePath(path.toString());
        toSave.setUser(user);

        return save(toSave);
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
    public List<Picture> findAllBySearchId(User user, Long searchId) {
        //TODO: refactor
        return pictureRepository.findAllByUser(user);
    }

    @Override
    public InputStream getPictureContentStreamById(Long id) {
        String filePath = pictureRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Picture with id " + id + " is not found"))
            .getFilePath();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new RuntimeException("Can't get file with path " + filePath);
        }

        return inputStream;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
    }
}
