package com.kowechka.petsearch.service;

import com.kowechka.petsearch.domain.Dog;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dog}.
 */
public interface DogService {
    /**
     * Save a dog.
     *
     * @param dog the entity to save.
     * @return the persisted entity.
     */
    Dog save(Dog dog);

    /**
     * Partially updates a dog.
     *
     * @param dog the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dog> partialUpdate(Dog dog);

    /**
     * Get all the dogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dog> findAll(Pageable pageable);

    /**
     * Get the "id" dog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dog> findOne(Long id);

    /**
     * Delete the "id" dog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
