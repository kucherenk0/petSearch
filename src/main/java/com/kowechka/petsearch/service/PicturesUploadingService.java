package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.PicturesUploading;
import com.kowechka.petsearch.web.rest.dto.PictureDto;
import com.kowechka.petsearch.web.rest.dto.PicturesUploadingDto;
import java.util.List;
import java.util.Optional;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.web.multipart.MultipartFile;

public interface PicturesUploadingService {
    PicturesUploadingDto uploadFilesAndCreatePictures(MultipartFile[] files);

    Optional<PicturesUploading> findOne(Long id);

    Optional<PicturesUploadingDto> findOneAndMapToDto(Long id);

    PicturesUploading save(PicturesUploading picturesUploading);

    PicturesUploadingDto toDto(
        PicturesUploading picturesUploading,
        List<PictureDto> pictureDtos
    );
}
