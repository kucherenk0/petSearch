package com.kowechka.petsearch.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.service.MlPictureClassifierService;
import com.kowechka.petsearch.service.PictureService;
import com.kowechka.petsearch.service.dto.ClassificationResultDto;
import com.kowechka.petsearch.service.exception.MlServiceException;
import io.micrometer.core.instrument.search.Search;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MlPictureClassifierServiceImpl implements MlPictureClassifierService {

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final PictureService pictureService;

    public static final String PYTHON_PICTURE_SCRIPT_FILE_NAME = "/ml/classifier.py";

    public static final String PYTHON_SCRIPT_FILE_NAME = "ml/classifier.py";
    public static final String RESULT_FOLDER_PATH = "src/../ml/results/";
    public static final String PYTHON_OUTPUT_STREAM_FILE = "ml/output.txt";

    @Override
    public void sendSearchToClassification(PetSearchEntity search) {
        log.debug("Request to execute classification for search: {}", search);
        executePythonScriptForSearch(search);
        //        search
        //            .getPictures()
        //            .forEach(p ->
        //                //passing args in format: photo_id file_path
        //                executePythonScriptForPictures(search.getId().toString(), p.getId().toString(), p.getFilePath())
        //            );
    }

    @Override
    public void sendPictureToClassification(String path) {
        executePythonScriptForPicture(path);
    }

    @Override
    public Boolean isCompleted(PetSearchEntity search) {
        //        var pictures = pictureService.findAllByPetSearchId(search.getId());
        //        return pictures.stream().allMatch(picture -> isResultPresentForPicture(picture, search.getId().toString()));
        return isResultPresentForSearch(search);
    }

    @Override
    public List<ClassificationResultDto> getClassificationResult(PetSearchEntity search) {
        if (!isCompleted(search)) {
            throw new MlServiceException(
                "Result is not ready for search with id" + search.getId(),
                null
            );
        }
        var folderPath = RESULT_FOLDER_PATH + "search-" + search.getId() + "/";

        var result = new ArrayList<ClassificationResultDto>();
        var resultFile = new File(folderPath + "/result.json");
        try {
            result.addAll(getClassificationResultDto(resultFile));
        } catch (IOException e) {
            throw new MlServiceException(
                "Can't read from file " + resultFile.getPath(),
                e
            );
        }
        return result;
    }

    @Override
    public List<ClassificationResultDto> getClassificationResultForPictures(
        PetSearchEntity search
    ) {
        if (!isCompleted(search)) {
            throw new MlServiceException(
                "Result is not ready for search with id" + search.getId(),
                null
            );
        }
        //TODO: change to formatting
        var folderPath = RESULT_FOLDER_PATH + "search-" + search.getId() + "/";
        var pictures = pictureService.findAllByPetSearchId(search.getId());
        var result = new ArrayList<ClassificationResultDto>();
        pictures.forEach(p -> {
            var resultFile = new File(folderPath + "/pic-" + p.getId() + ".json");
            try {
                result.addAll(getClassificationResultDto(resultFile));
            } catch (IOException e) {
                throw new MlServiceException(
                    "Can't read from file " + resultFile.getPath(),
                    e
                );
            }
        });

        return result;
    }

    private List<ClassificationResultDto> getClassificationResultDto(File resultFile)
        throws IOException {
        var json = new String(Files.readAllBytes(resultFile.toPath()));
        ObjectMapper objectMapper = new ObjectMapper();

        ClassificationResultDto[] dtos = objectMapper.readValue(
            json,
            ClassificationResultDto[].class
        );

        return Arrays.asList(dtos);
    }

    private void executePythonScriptForPictures(
        String searchId,
        String pictureId,
        String fileName
    ) {
        log.debug(
            "Running new Python process with parameters: {}",
            pictureId + " " + fileName
        );

        ProcessBuilder processBuilder = new ProcessBuilder(
            "python3",
            PYTHON_SCRIPT_FILE_NAME,
            searchId,
            pictureId,
            fileName
        );
        processBuilder.redirectErrorStream(true);

        File outputFile = new File(PYTHON_OUTPUT_STREAM_FILE);
        processBuilder.redirectOutput(outputFile);

        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new MlServiceException(
                "Error during executing python script with params " +
                pictureId +
                " " +
                fileName,
                e
            );
        }
    }

    private void executePythonScriptForSearch(PetSearchEntity search) {
        log.debug("Running new Python process for search: {}", search);

        ProcessBuilder processBuilder = new ProcessBuilder(
            "python3",
            PYTHON_SCRIPT_FILE_NAME,
            "--time",
            search
                .getDateOfLost()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss")),
            "--color",
            String.valueOf(search.getColor()),
            "--tail",
            String.valueOf(search.getTail()),
            "--address",
            search.getAdderss(),
            "--search_radius",
            String.valueOf(search.getRadius()),
            "--search_path",
            RESULT_FOLDER_PATH + "/search-" + search.getId() + "/result.json"
        );

        processBuilder.redirectErrorStream(true);

        File outputFile = new File(PYTHON_OUTPUT_STREAM_FILE);
        processBuilder.redirectOutput(outputFile);

        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new MlServiceException(
                "Error during executing python script with search " + search,
                e
            );
        }
    }

    private void executePythonScriptForPicture(String path) {
        ProcessBuilder processBuilder = new ProcessBuilder(
            "python3",
            PYTHON_SCRIPT_FILE_NAME,
            "--mode add " + "--img_path " + path
        );
        processBuilder.redirectErrorStream(true);

        File outputFile = new File(PYTHON_OUTPUT_STREAM_FILE);
        processBuilder.redirectOutput(outputFile);

        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new MlServiceException(
                "Error during executing python script to add photo ",
                e
            );
        }
    }

    private boolean isResultPresentForPicture(Picture picture, String searchId) {
        log.debug(
            "Checking for result of classification Python process for file with path {} :",
            picture.getFilePath()
        );

        var resultFilePath =
            RESULT_FOLDER_PATH +
            "search-" +
            searchId +
            "/pic-" +
            picture.getId() +
            ".json";

        var f = new File(resultFilePath);
        return f.exists();
    }

    private boolean isResultPresentForSearch(PetSearchEntity search) {
        log.debug(
            "Checking for result of classification Python process for search {} :",
            search
        );

        var resultFilePath =
            RESULT_FOLDER_PATH + "search-" + search.getId() + "/result.json";

        var f = new File(resultFilePath);
        return f.exists();
    }
}
