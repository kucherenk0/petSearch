package com.kowechka.petsearch.service.impl;

import com.kowechka.petsearch.domain.Dog;
import com.kowechka.petsearch.repository.DogRepository;
import com.kowechka.petsearch.service.DogService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dog}.
 */
@Service
@Transactional
public class DogServiceImpl implements DogService {

    private final Logger log = LoggerFactory.getLogger(DogServiceImpl.class);

    private final DogRepository dogRepository;

    public DogServiceImpl(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @Override
    public Dog save(Dog dog) {
        log.debug("Request to save Dog : {}", dog);
        return dogRepository.save(dog);
    }

    @Override
    public Optional<Dog> partialUpdate(Dog dog) {
        log.debug("Request to partially update Dog : {}", dog);

        return dogRepository
            .findById(dog.getId())
            .map(existingDog -> {
                if (dog.getColor() != null) {
                    existingDog.setColor(dog.getColor());
                }
                if (dog.getDogBreed() != null) {
                    existingDog.setDogBreed(dog.getDogBreed());
                }
                if (dog.getLongTail() != null) {
                    existingDog.setLongTail(dog.getLongTail());
                }
                if (dog.getHasLeash() != null) {
                    existingDog.setHasLeash(dog.getHasLeash());
                }
                if (dog.getCoordinates() != null) {
                    existingDog.setCoordinates(dog.getCoordinates());
                }

                return existingDog;
            })
            .map(dogRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dog> findAll(Pageable pageable) {
        log.debug("Request to get all Dogs");
        return dogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dog> findOne(Long id) {
        log.debug("Request to get Dog : {}", id);
        return dogRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dog : {}", id);
        dogRepository.deleteById(id);
    }
}
