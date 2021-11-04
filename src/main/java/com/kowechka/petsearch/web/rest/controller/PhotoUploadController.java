package com.kowechka.petsearch.web.rest.controller;

import com.kowechka.petsearch.service.PicturesUploadingService;
import com.kowechka.petsearch.web.rest.dto.PicturesUploadingDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.ResponseUtil;

@AllArgsConstructor
@RestController
@RequestMapping("/api/")
@Slf4j
public class PhotoUploadController {

    private final PicturesUploadingService picturesUploadingService;

    @PostMapping("/pictures/upload")
    public ResponseEntity<PicturesUploadingDto> uploadPictures(
        @RequestParam("files") MultipartFile[] files
    ) {
        return ResponseEntity.ok(
            picturesUploadingService.uploadFilesAndCreatePictures(files)
        );
    }

    @GetMapping("/pictures/upload/{id}")
    public ResponseEntity<PicturesUploadingDto> get(@PathVariable("id") Long id) {
        return ResponseUtil.wrapOrNotFound(
            picturesUploadingService.findOneAndMapToDto(id)
        );
    }
}
