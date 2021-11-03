package com.kowechka.petsearch.service.client;

import com.kowechka.petsearch.domain.PetSearchEntity;
import com.kowechka.petsearch.service.dto.MlSearchRequestDto;
import com.kowechka.petsearch.service.dto.MlSearchResponseDto;
import com.kowechka.petsearch.service.exception.MlServiceException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MlServiceClientImpl implements MlServiceClient {

    //TODO: подтягивать из конфигов
    public static final String ML_SERVICE_BASE_URL = "http://127.0.0.1:5000";
    public static final String SEARCH_URL = ML_SERVICE_BASE_URL + "/search";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy-HH:mm:ss"
    );

    public static final HttpStatus SEARCH_SUCCESS_STATUS = HttpStatus.OK;

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

            if (response.getStatusCode() != SEARCH_SUCCESS_STATUS) {
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
            throw new MlServiceException(
                String.format(
                    "Ml Service with address %s is unavailable. Is it alive?",
                    ML_SERVICE_BASE_URL
                )
            );
        }
    }
}
