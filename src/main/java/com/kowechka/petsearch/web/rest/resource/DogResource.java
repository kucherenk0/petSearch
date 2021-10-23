package com.kowechka.petsearch.web.rest.resource;

import com.kowechka.petsearch.domain.Dog;
import com.kowechka.petsearch.repository.DogRepository;
import com.kowechka.petsearch.service.DogService;
import com.kowechka.petsearch.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kowechka.petsearch.domain.Dog}.
 */
@RestController
@RequestMapping("/api")
public class DogResource {

    private final Logger log = LoggerFactory.getLogger(DogResource.class);

    private static final String ENTITY_NAME = "dog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DogService dogService;

    private final DogRepository dogRepository;

    public DogResource(DogService dogService, DogRepository dogRepository) {
        this.dogService = dogService;
        this.dogRepository = dogRepository;
    }

    /**
     * {@code POST  /dogs} : Create a new dog.
     *
     * @param dog the dog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dog, or with status {@code 400 (Bad Request)} if the dog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dogs")
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) throws URISyntaxException {
        log.debug("REST request to save Dog : {}", dog);
        if (dog.getId() != null) {
            throw new BadRequestAlertException("A new dog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dog result = dogService.save(dog);
        return ResponseEntity
            .created(new URI("/api/dogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dogs/:id} : Updates an existing dog.
     *
     * @param id the id of the dog to save.
     * @param dog the dog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dog,
     * or with status {@code 400 (Bad Request)} if the dog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dogs/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dog dog)
        throws URISyntaxException {
        log.debug("REST request to update Dog : {}, {}", id, dog);
        if (dog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dog result = dogService.save(dog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dogs/:id} : Partial updates given fields of an existing dog, field will ignore if it is null
     *
     * @param id the id of the dog to save.
     * @param dog the dog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dog,
     * or with status {@code 400 (Bad Request)} if the dog is not valid,
     * or with status {@code 404 (Not Found)} if the dog is not found,
     * or with status {@code 500 (Internal Server Error)} if the dog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dogs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dog> partialUpdateDog(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dog dog)
        throws URISyntaxException {
        log.debug("REST request to partial update Dog partially : {}, {}", id, dog);
        if (dog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dog> result = dogService.partialUpdate(dog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dog.getId().toString())
        );
    }

    /**
     * {@code GET  /dogs} : get all the dogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dogs in body.
     */
    @GetMapping("/dogs")
    public ResponseEntity<List<Dog>> getAllDogs(Pageable pageable) {
        log.debug("REST request to get a page of Dogs");
        Page<Dog> page = dogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dogs/:id} : get the "id" dog.
     *
     * @param id the id of the dog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dogs/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        log.debug("REST request to get Dog : {}", id);
        Optional<Dog> dog = dogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dog);
    }

    /**
     * {@code DELETE  /dogs/:id} : delete the "id" dog.
     *
     * @param id the id of the dog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        log.debug("REST request to delete Dog : {}", id);
        dogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
