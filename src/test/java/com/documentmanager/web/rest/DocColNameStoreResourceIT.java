package com.documentmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.documentmanager.IntegrationTest;
import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocColNameStoreRepository;
import com.documentmanager.service.criteria.DocColNameStoreCriteria;
import com.documentmanager.service.dto.DocColNameStoreDTO;
import com.documentmanager.service.mapper.DocColNameStoreMapper;
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
 * Integration tests for the {@link DocColNameStoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocColNameStoreResourceIT {

    private static final String DEFAULT_COL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-col-name-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocColNameStoreRepository docColNameStoreRepository;

    @Autowired
    private DocColNameStoreMapper docColNameStoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocColNameStoreMockMvc;

    private DocColNameStore docColNameStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocColNameStore createEntity(EntityManager em) {
        DocColNameStore docColNameStore = new DocColNameStore().colName(DEFAULT_COL_NAME);
        // Add required entity
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docStore = DocStoreResourceIT.createEntity(em);
            em.persist(docStore);
            em.flush();
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        docColNameStore.setDocStore(docStore);
        return docColNameStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocColNameStore createUpdatedEntity(EntityManager em) {
        DocColNameStore docColNameStore = new DocColNameStore().colName(UPDATED_COL_NAME);
        // Add required entity
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docStore = DocStoreResourceIT.createUpdatedEntity(em);
            em.persist(docStore);
            em.flush();
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        docColNameStore.setDocStore(docStore);
        return docColNameStore;
    }

    @BeforeEach
    public void initTest() {
        docColNameStore = createEntity(em);
    }

    @Test
    @Transactional
    void createDocColNameStore() throws Exception {
        int databaseSizeBeforeCreate = docColNameStoreRepository.findAll().size();
        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);
        restDocColNameStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeCreate + 1);
        DocColNameStore testDocColNameStore = docColNameStoreList.get(docColNameStoreList.size() - 1);
        assertThat(testDocColNameStore.getColName()).isEqualTo(DEFAULT_COL_NAME);
    }

    @Test
    @Transactional
    void createDocColNameStoreWithExistingId() throws Exception {
        // Create the DocColNameStore with an existing ID
        docColNameStore.setId(1L);
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        int databaseSizeBeforeCreate = docColNameStoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocColNameStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkColNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = docColNameStoreRepository.findAll().size();
        // set the field null
        docColNameStore.setColName(null);

        // Create the DocColNameStore, which fails.
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        restDocColNameStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isBadRequest());

        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocColNameStores() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get all the docColNameStoreList
        restDocColNameStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docColNameStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].colName").value(hasItem(DEFAULT_COL_NAME)));
    }

    @Test
    @Transactional
    void getDocColNameStore() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get the docColNameStore
        restDocColNameStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, docColNameStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docColNameStore.getId().intValue()))
            .andExpect(jsonPath("$.colName").value(DEFAULT_COL_NAME));
    }

    @Test
    @Transactional
    void getDocColNameStoresByIdFiltering() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        Long id = docColNameStore.getId();

        defaultDocColNameStoreShouldBeFound("id.equals=" + id);
        defaultDocColNameStoreShouldNotBeFound("id.notEquals=" + id);

        defaultDocColNameStoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocColNameStoreShouldNotBeFound("id.greaterThan=" + id);

        defaultDocColNameStoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocColNameStoreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocColNameStoresByColNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get all the docColNameStoreList where colName equals to DEFAULT_COL_NAME
        defaultDocColNameStoreShouldBeFound("colName.equals=" + DEFAULT_COL_NAME);

        // Get all the docColNameStoreList where colName equals to UPDATED_COL_NAME
        defaultDocColNameStoreShouldNotBeFound("colName.equals=" + UPDATED_COL_NAME);
    }

    @Test
    @Transactional
    void getAllDocColNameStoresByColNameIsInShouldWork() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get all the docColNameStoreList where colName in DEFAULT_COL_NAME or UPDATED_COL_NAME
        defaultDocColNameStoreShouldBeFound("colName.in=" + DEFAULT_COL_NAME + "," + UPDATED_COL_NAME);

        // Get all the docColNameStoreList where colName equals to UPDATED_COL_NAME
        defaultDocColNameStoreShouldNotBeFound("colName.in=" + UPDATED_COL_NAME);
    }

    @Test
    @Transactional
    void getAllDocColNameStoresByColNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get all the docColNameStoreList where colName is not null
        defaultDocColNameStoreShouldBeFound("colName.specified=true");

        // Get all the docColNameStoreList where colName is null
        defaultDocColNameStoreShouldNotBeFound("colName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocColNameStoresByColNameContainsSomething() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get all the docColNameStoreList where colName contains DEFAULT_COL_NAME
        defaultDocColNameStoreShouldBeFound("colName.contains=" + DEFAULT_COL_NAME);

        // Get all the docColNameStoreList where colName contains UPDATED_COL_NAME
        defaultDocColNameStoreShouldNotBeFound("colName.contains=" + UPDATED_COL_NAME);
    }

    @Test
    @Transactional
    void getAllDocColNameStoresByColNameNotContainsSomething() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        // Get all the docColNameStoreList where colName does not contain DEFAULT_COL_NAME
        defaultDocColNameStoreShouldNotBeFound("colName.doesNotContain=" + DEFAULT_COL_NAME);

        // Get all the docColNameStoreList where colName does not contain UPDATED_COL_NAME
        defaultDocColNameStoreShouldBeFound("colName.doesNotContain=" + UPDATED_COL_NAME);
    }

    @Test
    @Transactional
    void getAllDocColNameStoresByDocStoreIsEqualToSomething() throws Exception {
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docColNameStoreRepository.saveAndFlush(docColNameStore);
            docStore = DocStoreResourceIT.createEntity(em);
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        em.persist(docStore);
        em.flush();
        docColNameStore.setDocStore(docStore);
        docColNameStoreRepository.saveAndFlush(docColNameStore);
        Long docStoreId = docStore.getId();

        // Get all the docColNameStoreList where docStore equals to docStoreId
        defaultDocColNameStoreShouldBeFound("docStoreId.equals=" + docStoreId);

        // Get all the docColNameStoreList where docStore equals to (docStoreId + 1)
        defaultDocColNameStoreShouldNotBeFound("docStoreId.equals=" + (docStoreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocColNameStoreShouldBeFound(String filter) throws Exception {
        restDocColNameStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docColNameStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].colName").value(hasItem(DEFAULT_COL_NAME)));

        // Check, that the count call also returns 1
        restDocColNameStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocColNameStoreShouldNotBeFound(String filter) throws Exception {
        restDocColNameStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocColNameStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocColNameStore() throws Exception {
        // Get the docColNameStore
        restDocColNameStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocColNameStore() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();

        // Update the docColNameStore
        DocColNameStore updatedDocColNameStore = docColNameStoreRepository.findById(docColNameStore.getId()).get();
        // Disconnect from session so that the updates on updatedDocColNameStore are not directly saved in db
        em.detach(updatedDocColNameStore);
        updatedDocColNameStore.colName(UPDATED_COL_NAME);
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(updatedDocColNameStore);

        restDocColNameStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docColNameStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
        DocColNameStore testDocColNameStore = docColNameStoreList.get(docColNameStoreList.size() - 1);
        assertThat(testDocColNameStore.getColName()).isEqualTo(UPDATED_COL_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDocColNameStore() throws Exception {
        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();
        docColNameStore.setId(count.incrementAndGet());

        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocColNameStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docColNameStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocColNameStore() throws Exception {
        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();
        docColNameStore.setId(count.incrementAndGet());

        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColNameStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocColNameStore() throws Exception {
        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();
        docColNameStore.setId(count.incrementAndGet());

        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColNameStoreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocColNameStoreWithPatch() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();

        // Update the docColNameStore using partial update
        DocColNameStore partialUpdatedDocColNameStore = new DocColNameStore();
        partialUpdatedDocColNameStore.setId(docColNameStore.getId());

        restDocColNameStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocColNameStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocColNameStore))
            )
            .andExpect(status().isOk());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
        DocColNameStore testDocColNameStore = docColNameStoreList.get(docColNameStoreList.size() - 1);
        assertThat(testDocColNameStore.getColName()).isEqualTo(DEFAULT_COL_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDocColNameStoreWithPatch() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();

        // Update the docColNameStore using partial update
        DocColNameStore partialUpdatedDocColNameStore = new DocColNameStore();
        partialUpdatedDocColNameStore.setId(docColNameStore.getId());

        partialUpdatedDocColNameStore.colName(UPDATED_COL_NAME);

        restDocColNameStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocColNameStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocColNameStore))
            )
            .andExpect(status().isOk());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
        DocColNameStore testDocColNameStore = docColNameStoreList.get(docColNameStoreList.size() - 1);
        assertThat(testDocColNameStore.getColName()).isEqualTo(UPDATED_COL_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDocColNameStore() throws Exception {
        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();
        docColNameStore.setId(count.incrementAndGet());

        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocColNameStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docColNameStoreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocColNameStore() throws Exception {
        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();
        docColNameStore.setId(count.incrementAndGet());

        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColNameStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocColNameStore() throws Exception {
        int databaseSizeBeforeUpdate = docColNameStoreRepository.findAll().size();
        docColNameStore.setId(count.incrementAndGet());

        // Create the DocColNameStore
        DocColNameStoreDTO docColNameStoreDTO = docColNameStoreMapper.toDto(docColNameStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColNameStoreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docColNameStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocColNameStore in the database
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocColNameStore() throws Exception {
        // Initialize the database
        docColNameStoreRepository.saveAndFlush(docColNameStore);

        int databaseSizeBeforeDelete = docColNameStoreRepository.findAll().size();

        // Delete the docColNameStore
        restDocColNameStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, docColNameStore.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocColNameStore> docColNameStoreList = docColNameStoreRepository.findAll();
        assertThat(docColNameStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
