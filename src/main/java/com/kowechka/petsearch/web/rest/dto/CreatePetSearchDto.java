package com.kowechka.petsearch.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreatePetSearchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MultipartFile> filesToUpload;
    private LocalDate dateOfLost;
    private String address;
}
