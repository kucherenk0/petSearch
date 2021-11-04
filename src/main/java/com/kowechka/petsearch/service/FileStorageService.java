package com.kowechka.petsearch.service;

import com.kowechka.petsearch.service.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file) throws FileStorageException;
}
