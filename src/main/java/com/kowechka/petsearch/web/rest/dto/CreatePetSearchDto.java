package com.kowechka.petsearch.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CreatePetSearchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MultipartFile> filesToUpload = new ArrayList<>();
    private LocalDateTime dateOfLost;
    private String address;
    private int color;
    private int tail;
    private int radius;
}
