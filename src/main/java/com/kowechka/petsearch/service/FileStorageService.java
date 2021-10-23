package com.kowechka.petsearch.service;

import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file);
}
