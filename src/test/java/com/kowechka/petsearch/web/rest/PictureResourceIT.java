package com.kowechka.petsearch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kowechka.petsearch.IntegrationTest;
import com.kowechka.petsearch.domain.Picture;
import com.kowechka.petsearch.repository.PictureRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PictureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PictureResourceIT {

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_DOG = false;
    private static final Boolean UPDATED_HAS_DOG = true;

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CAMERA_ID = "AAAAAAAAAA";
    private static final String UPDATED_CAMERA_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_SHOOT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_SHOOT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/pictures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPictureMockMvc;

    private Picture picture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Picture createEntity(EntityManager em) {
        Picture picture = new Picture()
            .externalId(DEFAULT_EXTERNAL_ID)
            .hasDog(DEFAULT_HAS_DOG)
            .filePath(DEFAULT_FILE_PATH)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .cameraId(DEFAULT_CAMERA_ID)
            .dateOfShoot(DEFAULT_DATE_OF_SHOOT);
        return picture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Picture createUpdatedEntity(EntityManager em) {
        Picture picture = new Picture()
            .externalId(UPDATED_EXTERNAL_ID)
            .hasDog(UPDATED_HAS_DOG)
            .filePath(UPDATED_FILE_PATH)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .cameraId(UPDATED_CAMERA_ID)
            .dateOfShoot(UPDATED_DATE_OF_SHOOT);
        return picture;
    }

    @BeforeEach
    public void initTest() {
        picture = createEntity(em);
    }

    @Test
    @Transactional
    void createPicture() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();
        // Create the Picture
        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isCreated());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeCreate + 1);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testPicture.getHasDog()).isEqualTo(DEFAULT_HAS_DOG);
        assertThat(testPicture.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testPicture.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testPicture.getCameraId()).isEqualTo(DEFAULT_CAMERA_ID);
        assertThat(testPicture.getDateOfShoot()).isEqualTo(DEFAULT_DATE_OF_SHOOT);
    }

    @Test
    @Transactional
    void createPictureWithExistingId() throws Exception {
        // Create the Picture with an existing ID
        picture.setId(1L);

        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setExternalId(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = pictureRepository.findAll().size();
        // set the field null
        picture.setFilePath(null);

        // Create the Picture, which fails.

        restPictureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isBadRequest());

        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPictures() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get all the pictureList
        restPictureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(picture.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].hasDog").value(hasItem(DEFAULT_HAS_DOG.booleanValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].cameraId").value(hasItem(DEFAULT_CAMERA_ID)))
            .andExpect(jsonPath("$.[*].dateOfShoot").value(hasItem(DEFAULT_DATE_OF_SHOOT.toString())));
    }

    @Test
    @Transactional
    void getPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        // Get the picture
        restPictureMockMvc
            .perform(get(ENTITY_API_URL_ID, picture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(picture.getId().intValue()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.hasDog").value(DEFAULT_HAS_DOG.booleanValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.cameraId").value(DEFAULT_CAMERA_ID))
            .andExpect(jsonPath("$.dateOfShoot").value(DEFAULT_DATE_OF_SHOOT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPicture() throws Exception {
        // Get the picture
        restPictureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture
        Picture updatedPicture = pictureRepository.findById(picture.getId()).get();
        // Disconnect from session so that the updates on updatedPicture are not directly saved in db
        em.detach(updatedPicture);
        updatedPicture
            .externalId(UPDATED_EXTERNAL_ID)
            .hasDog(UPDATED_HAS_DOG)
            .filePath(UPDATED_FILE_PATH)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .cameraId(UPDATED_CAMERA_ID)
            .dateOfShoot(UPDATED_DATE_OF_SHOOT);

        restPictureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPicture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPicture))
            )
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testPicture.getHasDog()).isEqualTo(UPDATED_HAS_DOG);
        assertThat(testPicture.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testPicture.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testPicture.getCameraId()).isEqualTo(UPDATED_CAMERA_ID);
        assertThat(testPicture.getDateOfShoot()).isEqualTo(UPDATED_DATE_OF_SHOOT);
    }

    @Test
    @Transactional
    void putNonExistingPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, picture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePictureWithPatch() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture using partial update
        Picture partialUpdatedPicture = new Picture();
        partialUpdatedPicture.setId(picture.getId());

        partialUpdatedPicture.streetAddress(UPDATED_STREET_ADDRESS).dateOfShoot(UPDATED_DATE_OF_SHOOT);

        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPicture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPicture))
            )
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testPicture.getHasDog()).isEqualTo(DEFAULT_HAS_DOG);
        assertThat(testPicture.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testPicture.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testPicture.getCameraId()).isEqualTo(DEFAULT_CAMERA_ID);
        assertThat(testPicture.getDateOfShoot()).isEqualTo(UPDATED_DATE_OF_SHOOT);
    }

    @Test
    @Transactional
    void fullUpdatePictureWithPatch() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the picture using partial update
        Picture partialUpdatedPicture = new Picture();
        partialUpdatedPicture.setId(picture.getId());

        partialUpdatedPicture
            .externalId(UPDATED_EXTERNAL_ID)
            .hasDog(UPDATED_HAS_DOG)
            .filePath(UPDATED_FILE_PATH)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .cameraId(UPDATED_CAMERA_ID)
            .dateOfShoot(UPDATED_DATE_OF_SHOOT);

        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPicture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPicture))
            )
            .andExpect(status().isOk());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
        Picture testPicture = pictureList.get(pictureList.size() - 1);
        assertThat(testPicture.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testPicture.getHasDog()).isEqualTo(UPDATED_HAS_DOG);
        assertThat(testPicture.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testPicture.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testPicture.getCameraId()).isEqualTo(UPDATED_CAMERA_ID);
        assertThat(testPicture.getDateOfShoot()).isEqualTo(UPDATED_DATE_OF_SHOOT);
    }

    @Test
    @Transactional
    void patchNonExistingPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, picture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(picture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPicture() throws Exception {
        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();
        picture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPictureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(picture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Picture in the database
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(picture);

        int databaseSizeBeforeDelete = pictureRepository.findAll().size();

        // Delete the picture
        restPictureMockMvc
            .perform(delete(ENTITY_API_URL_ID, picture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Picture> pictureList = pictureRepository.findAll();
        assertThat(pictureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
