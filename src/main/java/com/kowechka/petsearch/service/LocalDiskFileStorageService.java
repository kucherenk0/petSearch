package com.kowechka.petsearch.service;

import com.kowechka.petsearch.service.exception.FileStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalDiskFileStorageService implements FileStorageService {

    //TODO: move to props
    public static final String FILE_STORAGE_FOLDER_PATH = "file-storage/";
    public static final String FILE_STORAGE_EXCEPTION_MESSAGE = "Can't persist file";

    @Override
    public String upload(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            //String newFileName = RandomStringUtils.randomAlphanumeric(8);
            String newFileName = RandomStringUtils.randomAlphanumeric(3) + file.getOriginalFilename();
            Path path = Path.of(FILE_STORAGE_FOLDER_PATH + newFileName).toAbsolutePath();
            Files.write(path, bytes);

            return path.toString();
        } catch (IOException e) {
            throw new FileStorageException(FILE_STORAGE_EXCEPTION_MESSAGE, e);
        }
    }
}