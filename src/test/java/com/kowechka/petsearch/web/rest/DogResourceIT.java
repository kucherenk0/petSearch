package com.kowechka.petsearch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kowechka.petsearch.IntegrationTest;
import com.kowechka.petsearch.domain.Dog;
import com.kowechka.petsearch.repository.DogRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DogResourceIT {

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_DOG_BREED = "AAAAAAAAAA";
    private static final String UPDATED_DOG_BREED = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LONG_TAIL = false;
    private static final Boolean UPDATED_LONG_TAIL = true;

    private static final Boolean DEFAULT_HAS_LEASH = false;
    private static final Boolean UPDATED_HAS_LEASH = true;

    private static final String DEFAULT_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_COORDINATES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDogMockMvc;

    private Dog dog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dog createEntity(EntityManager em) {
        Dog dog = new Dog()
            .color(DEFAULT_COLOR)
            .dogBreed(DEFAULT_DOG_BREED)
            .longTail(DEFAULT_LONG_TAIL)
            .hasLeash(DEFAULT_HAS_LEASH)
            .coordinates(DEFAULT_COORDINATES);
        return dog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dog createUpdatedEntity(EntityManager em) {
        Dog dog = new Dog()
            .color(UPDATED_COLOR)
            .dogBreed(UPDATED_DOG_BREED)
            .longTail(UPDATED_LONG_TAIL)
            .hasLeash(UPDATED_HAS_LEASH)
            .coordinates(UPDATED_COORDINATES);
        return dog;
    }

    @BeforeEach
    public void initTest() {
        dog = createEntity(em);
    }

    @Test
    @Transactional
    void createDog() throws Exception {
        int databaseSizeBeforeCreate = dogRepository.findAll().size();
        // Create the Dog
        restDogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isCreated());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeCreate + 1);
        Dog testDog = dogList.get(dogList.size() - 1);
        assertThat(testDog.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testDog.getDogBreed()).isEqualTo(DEFAULT_DOG_BREED);
        assertThat(testDog.getLongTail()).isEqualTo(DEFAULT_LONG_TAIL);
        assertThat(testDog.getHasLeash()).isEqualTo(DEFAULT_HAS_LEASH);
        assertThat(testDog.getCoordinates()).isEqualTo(DEFAULT_COORDINATES);
    }

    @Test
    @Transactional
    void createDogWithExistingId() throws Exception {
        // Create the Dog with an existing ID
        dog.setId(1L);

        int databaseSizeBeforeCreate = dogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDogs() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        // Get all the dogList
        restDogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dog.getId().intValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].dogBreed").value(hasItem(DEFAULT_DOG_BREED)))
            .andExpect(jsonPath("$.[*].longTail").value(hasItem(DEFAULT_LONG_TAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].hasLeash").value(hasItem(DEFAULT_HAS_LEASH.booleanValue())))
            .andExpect(jsonPath("$.[*].coordinates").value(hasItem(DEFAULT_COORDINATES)));
    }

    @Test
    @Transactional
    void getDog() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        // Get the dog
        restDogMockMvc
            .perform(get(ENTITY_API_URL_ID, dog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dog.getId().intValue()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.dogBreed").value(DEFAULT_DOG_BREED))
            .andExpect(jsonPath("$.longTail").value(DEFAULT_LONG_TAIL.booleanValue()))
            .andExpect(jsonPath("$.hasLeash").value(DEFAULT_HAS_LEASH.booleanValue()))
            .andExpect(jsonPath("$.coordinates").value(DEFAULT_COORDINATES));
    }

    @Test
    @Transactional
    void getNonExistingDog() throws Exception {
        // Get the dog
        restDogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDog() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        int databaseSizeBeforeUpdate = dogRepository.findAll().size();

        // Update the dog
        Dog updatedDog = dogRepository.findById(dog.getId()).get();
        // Disconnect from session so that the updates on updatedDog are not directly saved in db
        em.detach(updatedDog);
        updatedDog
            .color(UPDATED_COLOR)
            .dogBreed(UPDATED_DOG_BREED)
            .longTail(UPDATED_LONG_TAIL)
            .hasLeash(UPDATED_HAS_LEASH)
            .coordinates(UPDATED_COORDINATES);

        restDogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDog))
            )
            .andExpect(status().isOk());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
        Dog testDog = dogList.get(dogList.size() - 1);
        assertThat(testDog.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testDog.getDogBreed()).isEqualTo(UPDATED_DOG_BREED);
        assertThat(testDog.getLongTail()).isEqualTo(UPDATED_LONG_TAIL);
        assertThat(testDog.getHasLeash()).isEqualTo(UPDATED_HAS_LEASH);
        assertThat(testDog.getCoordinates()).isEqualTo(UPDATED_COORDINATES);
    }

    @Test
    @Transactional
    void putNonExistingDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();
        dog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dog.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();
        dog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();
        dog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDogWithPatch() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        int databaseSizeBeforeUpdate = dogRepository.findAll().size();

        // Update the dog using partial update
        Dog partialUpdatedDog = new Dog();
        partialUpdatedDog.setId(dog.getId());

        partialUpdatedDog.coordinates(UPDATED_COORDINATES);

        restDogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDog))
            )
            .andExpect(status().isOk());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
        Dog testDog = dogList.get(dogList.size() - 1);
        assertThat(testDog.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testDog.getDogBreed()).isEqualTo(DEFAULT_DOG_BREED);
        assertThat(testDog.getLongTail()).isEqualTo(DEFAULT_LONG_TAIL);
        assertThat(testDog.getHasLeash()).isEqualTo(DEFAULT_HAS_LEASH);
        assertThat(testDog.getCoordinates()).isEqualTo(UPDATED_COORDINATES);
    }

    @Test
    @Transactional
    void fullUpdateDogWithPatch() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        int databaseSizeBeforeUpdate = dogRepository.findAll().size();

        // Update the dog using partial update
        Dog partialUpdatedDog = new Dog();
        partialUpdatedDog.setId(dog.getId());

        partialUpdatedDog
            .color(UPDATED_COLOR)
            .dogBreed(UPDATED_DOG_BREED)
            .longTail(UPDATED_LONG_TAIL)
            .hasLeash(UPDATED_HAS_LEASH)
            .coordinates(UPDATED_COORDINATES);

        restDogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDog))
            )
            .andExpect(status().isOk());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
        Dog testDog = dogList.get(dogList.size() - 1);
        assertThat(testDog.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testDog.getDogBreed()).isEqualTo(UPDATED_DOG_BREED);
        assertThat(testDog.getLongTail()).isEqualTo(UPDATED_LONG_TAIL);
        assertThat(testDog.getHasLeash()).isEqualTo(UPDATED_HAS_LEASH);
        assertThat(testDog.getCoordinates()).isEqualTo(UPDATED_COORDINATES);
    }

    @Test
    @Transactional
    void patchNonExistingDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();
        dog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();
        dog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDog() throws Exception {
        int databaseSizeBeforeUpdate = dogRepository.findAll().size();
        dog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dog in the database
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDog() throws Exception {
        // Initialize the database
        dogRepository.saveAndFlush(dog);

        int databaseSizeBeforeDelete = dogRepository.findAll().size();

        // Delete the dog
        restDogMockMvc.perform(delete(ENTITY_API_URL_ID, dog.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dog> dogList = dogRepository.findAll();
        assertThat(dogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
