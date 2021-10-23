package com.kowechka.petsearch.web.rest;

import com.kowechka.petsearch.service.PetSearchService;
import com.kowechka.petsearch.service.mapper.PetSearchDtoMapper;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.ResponseUtil;

@AllArgsConstructor
@RestController
@RequestMapping("/api/search")
@Slf4j
public class PetSearchController {

    private final PetSearchService petSearchService;
    private final PetSearchDtoMapper petSearchDtoMapper;

    @PostMapping("/")
    public ResponseEntity<PetSearchDto> create(@RequestBody CreatePetSearchDto dto) {
        return ResponseEntity.ok(petSearchDtoMapper.toDto(petSearchService.createEntityAndRunSearch(dto)));
    }

    @PostMapping("/form")
    public ResponseEntity<PetSearchDto> createPetSearchWithForm(
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("address") String address,
        @RequestParam("dateOfLost") LocalDate dateOfLost
    ) {
        var dto = CreatePetSearchDto.builder().filesToUpload(Arrays.asList(files)).address(address).dateOfLost(dateOfLost).build();
        return ResponseEntity.ok(petSearchDtoMapper.toDto(petSearchService.createEntityAndRunSearch(dto)));
    }

    @GetMapping("{id}")
    public ResponseEntity<PetSearchDto> get(@PathVariable("id") Long id) {
        return ResponseUtil.wrapOrNotFound(petSearchService.findOne(id));
    }
}
