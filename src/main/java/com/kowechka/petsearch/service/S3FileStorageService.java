package com.kowechka.petsearch.service;

import com.kowechka.petsearch.service.exception.FileStorageException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class S3FileStorageService implements FileStorageService {

    //TODO: получать из конфигов
    public static final Boolean USE_FAKE_S3_RESPONSE = true;

    private final List<String> fakeUrls = List.of(
        "https://storage.yandexcloud.net/psstorage/B54.jpg",
        "https://storage.yandexcloud.net/psstorage/123.jpg",
        "https://storage.yandexcloud.net/psstorage/124.jpg",
        "https://storage.yandexcloud.net/psstorage/125.jpg",
        "https://storage.yandexcloud.net/psstorage/124.jpg"
    );

    //TODO: connect real S3
    @Override
    public String upload(MultipartFile file) throws FileStorageException {
        if (USE_FAKE_S3_RESPONSE) {
            return getFakeUrl();
        }
        return null;
    }

    private String getFakeUrl() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, fakeUrls.size());
        return fakeUrls.get(randomIndex);
    }
}
