package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.domain.enumeration.SearchStatus;
import com.kowechka.petsearch.service.MlPictureClassifierService;
import com.kowechka.petsearch.service.PetSearchService;
import com.kowechka.petsearch.service.PictureService;
import com.kowechka.petsearch.service.exception.MlServiceException;
import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MlPictureClassifierServiceImpl implements MlPictureClassifierService {

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    public static final String PYTHON_SCRIPT_FILE_NAME = "ml/classifier.py";
    public static final String RESULT_FOLDER_PATH = "ml/results/";

    private final PictureService pictureService;
    private final PetSearchService petSearchService;

    @Override
    public void sendSearchToClassification(PetSearchEntity search) {
        log.debug("Request to execute classification for search: {}", search);

        search.getPictures().forEach(p -> executePythonScriptWithParameters(p.getFilePath()));
        search.setStatus(SearchStatus.IN_PROGRESS);
        petSearchService.save(search);
    }

    @Override
    public Boolean isCompleted(PetSearchEntity search) {
        return search.getPictures().stream().allMatch(this::isResultPresentForPicture);
    }

    private void executePythonScriptWithParameters(String paramString) {
        log.debug("Running new Python process with parameters: {}", paramString);

        ProcessBuilder processBuilder = new ProcessBuilder("python", PYTHON_SCRIPT_FILE_NAME, paramString);
        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new MlServiceException("Error during executing python script with params " + paramString, e);
        }
    }

    private boolean isResultPresentForPicture(Picture picture) {
        log.debug("Checking for result of classification Python process for file with path {} :", picture.getFilePath());

        var resultFilePath = RESULT_FOLDER_PATH + picture.getId();

        var f = new File(resultFilePath);
        var matchingFiles = f.listFiles((dir, name) -> name.equals(picture.getId() + ".json"));
        return (matchingFiles != null ? matchingFiles.length : 0) != 0;
    }
}
