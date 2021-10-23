package com.kowechka.petsearch.web.rest;

import com.kowechka.petsearch.service.PetSearchService;
import com.kowechka.petsearch.service.mapper.PetSearchDtoMapper;
import com.kowechka.petsearch.web.rest.dto.CreatePetSearchDto;
import com.kowechka.petsearch.web.rest.dto.PetSearchDto;
import java.util.Optional;
import liquibase.pro.packaged.O;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("{id}")
    public ResponseEntity<PetSearchDto> get(@PathVariable("id") Long id) {
        var entity = petSearchService.findOne(id);

        return entity
            .map(petSearchEntity -> ResponseEntity.ok(petSearchDtoMapper.toDto(petSearchEntity)))
            .orElseGet(() -> ResponseUtil.wrapOrNotFound(Optional.empty()));
    }
}
