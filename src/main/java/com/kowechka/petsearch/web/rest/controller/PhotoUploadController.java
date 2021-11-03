package com.kowechka.petsearch.web.rest.controller;

import com.kowechka.petsearch.service.PetSearchService;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

@AllArgsConstructor
@RestController
@RequestMapping("/api/")
@Slf4j
public class PhotoUploadController {

    private final PetSearchService petSearchService;

    @PostMapping("/search/dto")
    public ResponseEntity<PetSearchDto> create(@RequestBody CreatePetSearchDto dto) {
        return ResponseEntity.ok(petSearchService.createEntityAndRunSearch(dto));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<PetSearchDto> get(@PathVariable("id") Long id) {
        return ResponseUtil.wrapOrNotFound(petSearchService.findOne(id));
    }
}
