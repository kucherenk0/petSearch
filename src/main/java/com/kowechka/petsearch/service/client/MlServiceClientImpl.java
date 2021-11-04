package com.kowechka.petsearch.service.client;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.service.PictureEntityService;
import com.kowechka.petsearch.service.dto.MlProcessPhotosRequestDto;
import com.kowechka.petsearch.service.dto.MlSearchRequestDto;
import com.kowechka.petsearch.service.dto.MlSearchResponseDto;
import com.kowechka.petsearch.service.exception.MlServiceException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@AllArgsConstructor
public class MlServiceClientImpl implements MlServiceClient {

    public final PictureEntityService pictureEntityService;

    //TODO: подтягивать из конфигов
    public static final Boolean USE_FAKE_ML_RESPONSE = true;
    public static final String ML_SERVICE_BASE_URL = "http://127.0.0.1:5000";
    public static final String SEARCH_URL = ML_SERVICE_BASE_URL + "/search";
    public static final String PROCESS_PHOTOS_URL = ML_SERVICE_BASE_URL + "/process";
    public static final HttpStatus SUCCESS_STATUS = HttpStatus.OK;

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy-HH:mm:ss"
    );

    private final MlSearchResponseDto FAKE_ML_SERVICE_SEARCH_RESPONSE = MlSearchResponseDto
        .builder()
        .pictureIds(List.of(1L, 2L, 3L, 4L, 5L))
        .build();

    @Override
    public List<Long> getSearchResult(PetSearchEntity search) {
        RestTemplate restTemplate = new RestTemplate();

        MlSearchRequestDto requestDto = MlSearchRequestDto
            .builder()
            .dateOfLost(search.getDateOfLost().format(dateTimeFormatter))
            //TODO: поменять модель на double
            .radius((double) search.getRadius())
            .color(search.getColor())
            .address(search.getAddress())
            .tail(search.getTail())
            .build();

        HttpEntity<MlSearchRequestDto> request = new HttpEntity<>(requestDto);

        try {
            ResponseEntity<MlSearchResponseDto> response = restTemplate.exchange(
                SEARCH_URL,
                HttpMethod.GET,
                request,
                MlSearchResponseDto.class
            );

            if (response.getStatusCode() != SUCCESS_STATUS) {
                throw new MlServiceException(
                    String.format(
                        "Request POST /%s failed for entity %s",
                        SEARCH_URL,
                        requestDto
                    )
                );
            }

            MlSearchResponseDto searchResult = response.getBody();

            if (searchResult == null) {
                throw new MlServiceException(
                    String.format(
                        "Request POST /%s  for entity %s returned empty body",
                        SEARCH_URL,
                        requestDto
                    )
                );
            }
            return searchResult.getPictureIds();
        } catch (RestClientException e) {
            if (USE_FAKE_ML_RESPONSE) {
                log.error(e.getMessage());
                return FAKE_ML_SERVICE_SEARCH_RESPONSE.getPictureIds();
            }
            throw new MlServiceException(
                String.format(
                    "Ml Service with address %s is unavailable. Is it alive?",
                    ML_SERVICE_BASE_URL
                )
            );
        }
    }

    @Override
    public void sendPicturesToProcessing(List<Long> ids) {
        RestTemplate restTemplate = new RestTemplate();

        MlProcessPhotosRequestDto requestDto = MlProcessPhotosRequestDto
            .builder()
            .pictureIds(ids)
            .build();

        HttpEntity<MlProcessPhotosRequestDto> request = new HttpEntity<>(requestDto);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                PROCESS_PHOTOS_URL,
                HttpMethod.GET,
                request,
                String.class
            );

            if (response.getStatusCode() != SUCCESS_STATUS) {
                throw new MlServiceException(
                    String.format(
                        "Request POST /%s failed for entity %s",
                        PROCESS_PHOTOS_URL,
                        requestDto
                    )
                );
            }
        } catch (RestClientException e) {
            if (USE_FAKE_ML_RESPONSE) {
                log.error(e.getMessage());
                emulateMlServicePicturesProcess(ids);
                return;
            }
            throw new MlServiceException(
                String.format(
                    "Ml Service with address %s is unavailable. Is it alive?",
                    ML_SERVICE_BASE_URL
                )
            );
        }
    }

    private void emulateMlServicePicturesProcess(List<Long> pictureIds) {
        pictureIds
            .stream()
            .map(pictureEntityService::findOne)
            .filter(Optional::isPresent)
            .forEach(p -> {
                var picture = p.get();
                picture.setDate(LocalDateTime.now());
                picture.setLat("55.819172");
                picture.setLon("37.640728");
                picture.setAddress("Первомайская 32к2 это фейковые данные");
                picture.setCameraUid("fake cameraId");
                picture.setHasAnimal(true);
                picture.setHasDog(true);
                picture.setHasOwner(false);
                picture.setColor(0);
                picture.setTail(0);
                picture.setProcessed(true);
                pictureEntityService.save(picture);
            });
    }
}
