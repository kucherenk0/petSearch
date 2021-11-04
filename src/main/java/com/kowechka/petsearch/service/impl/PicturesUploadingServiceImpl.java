package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.PictureEntity;
import com.kowechka.petsearch.domain.PicturesUploading;
import com.kowechka.petsearch.domain.User;
import com.kowechka.petsearch.repository.PicturesUploadingRepository;
import com.kowechka.petsearch.service.FileStorageService;
import com.kowechka.petsearch.service.PictureEntityService;
import com.kowechka.petsearch.service.PicturesUploadingService;
import com.kowechka.petsearch.service.UserService;
import com.kowechka.petsearch.service.client.MlServiceClient;
import com.kowechka.petsearch.service.exception.CantGetCurrentUserException;
import com.kowechka.petsearch.service.exception.FileStorageException;
import com.kowechka.petsearch.web.rest.dto.PictureDto;
import com.kowechka.petsearch.web.rest.dto.PicturesUploadingDto;
import java.util.*;
import java.util.stream.Collectors;
import liquibase.pro.packaged.S;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class PicturesUploadingServiceImpl implements PicturesUploadingService {

    private final FileStorageService fileStorageService;
    private final PictureEntityService pictureEntityService;
    private final MlServiceClient mlServiceClient;
    private final PicturesUploadingRepository picturesUploadingRepository;
    private final UserService userService;

    @Override
    public PicturesUploadingDto uploadFilesAndCreatePictures(MultipartFile[] files) {
        User currentUser = userService
            .getUserWithAuthorities()
            .orElseThrow(CantGetCurrentUserException::new);

        var errors = new HashMap<String, String>();
        var downloadUrls = Arrays
            .stream(files)
            .map(f -> {
                try {
                    return fileStorageService.upload(f);
                } catch (FileStorageException e) {
                    log.error(e.getMessage());
                    errors.put(f.getName(), e.getMessage());
                    return null;
                }
            })
            .collect(Collectors.toList());

        var newPictureUploading = save(
            PicturesUploading
                .builder()
                .numberOfPictures(files.length)
                .processedPictures(0)
                .user(currentUser)
                .build()
        );

        var savedPictures = downloadUrls
            .stream()
            .filter(Objects::nonNull)
            .map(url ->
                pictureEntityService.save(
                    PictureEntity
                        .builder()
                        .downloadUrl(url)
                        .user(currentUser)
                        .picturesUploading(newPictureUploading)
                        .build()
                )
            )
            .collect(Collectors.toList());

        mlServiceClient.sendPicturesToProcessing(
            savedPictures.stream().map(PictureEntity::getId).collect(Collectors.toList())
        );

        var result = toDto(newPictureUploading, new ArrayList<PictureDto>());

        if (!errors.isEmpty()) {
            result.setResult(getPictureDtosWithErrors(errors));
        }

        return result;
    }

    @Override
    public Optional<PicturesUploading> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<PicturesUploadingDto> findOneAndMapToDto(Long id) {
        var mayBeEmpty = picturesUploadingRepository.findById(id);
        if (mayBeEmpty.isPresent()) {
            var result = mayBeEmpty.get();
            var pictures = pictureEntityService
                .findAllByUploadingId(result.getId())
                .stream()
                .filter(PictureEntity::getProcessed)
                .map(pictureEntityService::toDto)
                .collect(Collectors.toList());
            result.setProcessedPictures(pictures.size());
            save(result);
            return Optional.of(toDto(result, pictures));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public PicturesUploading save(PicturesUploading picturesUploading) {
        log.debug("Request to save PetSearchEntity : {}", picturesUploading);
        return picturesUploadingRepository.save(picturesUploading);
    }

    @Override
    public PicturesUploadingDto toDto(
        PicturesUploading picturesUploading,
        List<PictureDto> pictureDtos
    ) {
        return PicturesUploadingDto
            .builder()
            .id(picturesUploading.getId())
            .numberOfPictures(picturesUploading.getNumberOfPictures())
            .processedPictures(picturesUploading.getProcessedPictures())
            .result(pictureDtos)
            .build();
    }

    private List<PictureDto> getPictureDtosWithErrors(Map<String, String> errors) {
        var result = new ArrayList<PictureDto>();
        errors.forEach((k, v) -> result.add(PictureDto.builder().downloadUrl(v).build()));
        return result;
    }
}
