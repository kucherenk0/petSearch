package com.kowechka.petsearch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalDiskFileStorageService implements FileStorageService {

    @Override
    public String upload(MultipartFile file) {
        return null;
    }
}
