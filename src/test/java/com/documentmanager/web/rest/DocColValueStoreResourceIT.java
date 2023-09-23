package com.documentmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.documentmanager.IntegrationTest;
import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.domain.DocColValueStore;
import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocColValueStoreRepository;
import com.documentmanager.service.criteria.DocColValueStoreCriteria;
import com.documentmanager.service.dto.DocColValueStoreDTO;
import com.documentmanager.service.mapper.DocColValueStoreMapper;
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
 * Integration tests for the {@link DocColValueStoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocColValueStoreResourceIT {

    private static final String DEFAULT_COL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_COL_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-col-value-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocColValueStoreRepository docColValueStoreRepository;

    @Autowired
    private DocColValueStoreMapper docColValueStoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocColValueStoreMockMvc;

    private DocColValueStore docColValueStore;

    @BeforeEach
    public void initTest() {}

    @Test
    @Transactional
    void getAllDocColValueStores() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get all the docColValueStoreList
        restDocColValueStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docColValueStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].colValue").value(hasItem(DEFAULT_COL_VALUE)));
    }

    @Test
    @Transactional
    void getDocColValueStore() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get the docColValueStore
        restDocColValueStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, docColValueStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docColValueStore.getId().intValue()))
            .andExpect(jsonPath("$.colValue").value(DEFAULT_COL_VALUE));
    }

    @Test
    @Transactional
    void getDocColValueStoresByIdFiltering() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        Long id = docColValueStore.getId();

        defaultDocColValueStoreShouldBeFound("id.equals=" + id);
        defaultDocColValueStoreShouldNotBeFound("id.notEquals=" + id);

        defaultDocColValueStoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocColValueStoreShouldNotBeFound("id.greaterThan=" + id);

        defaultDocColValueStoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocColValueStoreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocColValueStoresByColValueIsEqualToSomething() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get all the docColValueStoreList where colValue equals to DEFAULT_COL_VALUE
        defaultDocColValueStoreShouldBeFound("colValue.equals=" + DEFAULT_COL_VALUE);

        // Get all the docColValueStoreList where colValue equals to UPDATED_COL_VALUE
        defaultDocColValueStoreShouldNotBeFound("colValue.equals=" + UPDATED_COL_VALUE);
    }

    @Test
    @Transactional
    void getAllDocColValueStoresByColValueIsInShouldWork() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get all the docColValueStoreList where colValue in DEFAULT_COL_VALUE or UPDATED_COL_VALUE
        defaultDocColValueStoreShouldBeFound("colValue.in=" + DEFAULT_COL_VALUE + "," + UPDATED_COL_VALUE);

        // Get all the docColValueStoreList where colValue equals to UPDATED_COL_VALUE
        defaultDocColValueStoreShouldNotBeFound("colValue.in=" + UPDATED_COL_VALUE);
    }

    @Test
    @Transactional
    void getAllDocColValueStoresByColValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get all the docColValueStoreList where colValue is not null
        defaultDocColValueStoreShouldBeFound("colValue.specified=true");

        // Get all the docColValueStoreList where colValue is null
        defaultDocColValueStoreShouldNotBeFound("colValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDocColValueStoresByColValueContainsSomething() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get all the docColValueStoreList where colValue contains DEFAULT_COL_VALUE
        defaultDocColValueStoreShouldBeFound("colValue.contains=" + DEFAULT_COL_VALUE);

        // Get all the docColValueStoreList where colValue contains UPDATED_COL_VALUE
        defaultDocColValueStoreShouldNotBeFound("colValue.contains=" + UPDATED_COL_VALUE);
    }

    @Test
    @Transactional
    void getAllDocColValueStoresByColValueNotContainsSomething() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        // Get all the docColValueStoreList where colValue does not contain DEFAULT_COL_VALUE
        defaultDocColValueStoreShouldNotBeFound("colValue.doesNotContain=" + DEFAULT_COL_VALUE);

        // Get all the docColValueStoreList where colValue does not contain UPDATED_COL_VALUE
        defaultDocColValueStoreShouldBeFound("colValue.doesNotContain=" + UPDATED_COL_VALUE);
    }

    @Test
    @Transactional
    void getAllDocColValueStoresByDocStoreIsEqualToSomething() throws Exception {
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docColValueStoreRepository.saveAndFlush(docColValueStore);
            docStore = DocStoreResourceIT.createEntity(em);
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        em.persist(docStore);
        em.flush();
        docColValueStore.setDocStore(docStore);
        docColValueStoreRepository.saveAndFlush(docColValueStore);
        Long docStoreId = docStore.getId();

        // Get all the docColValueStoreList where docStore equals to docStoreId
        defaultDocColValueStoreShouldBeFound("docStoreId.equals=" + docStoreId);

        // Get all the docColValueStoreList where docStore equals to (docStoreId + 1)
        defaultDocColValueStoreShouldNotBeFound("docStoreId.equals=" + (docStoreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocColValueStoreShouldBeFound(String filter) throws Exception {
        restDocColValueStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docColValueStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].colValue").value(hasItem(DEFAULT_COL_VALUE)));

        // Check, that the count call also returns 1
        restDocColValueStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocColValueStoreShouldNotBeFound(String filter) throws Exception {
        restDocColValueStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocColValueStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocColValueStore() throws Exception {
        // Get the docColValueStore
        restDocColValueStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocColValueStore() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();

        // Update the docColValueStore
        DocColValueStore updatedDocColValueStore = docColValueStoreRepository.findById(docColValueStore.getId()).get();
        // Disconnect from session so that the updates on updatedDocColValueStore are not directly saved in db
        em.detach(updatedDocColValueStore);
        updatedDocColValueStore.colValue(UPDATED_COL_VALUE);
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(updatedDocColValueStore);

        restDocColValueStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docColValueStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
        DocColValueStore testDocColValueStore = docColValueStoreList.get(docColValueStoreList.size() - 1);
        assertThat(testDocColValueStore.getColValue()).isEqualTo(UPDATED_COL_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingDocColValueStore() throws Exception {
        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();
        docColValueStore.setId(count.incrementAndGet());

        // Create the DocColValueStore
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(docColValueStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocColValueStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docColValueStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocColValueStore() throws Exception {
        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();
        docColValueStore.setId(count.incrementAndGet());

        // Create the DocColValueStore
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(docColValueStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColValueStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocColValueStore() throws Exception {
        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();
        docColValueStore.setId(count.incrementAndGet());

        // Create the DocColValueStore
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(docColValueStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColValueStoreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocColValueStoreWithPatch() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();

        // Update the docColValueStore using partial update
        DocColValueStore partialUpdatedDocColValueStore = new DocColValueStore();
        partialUpdatedDocColValueStore.setId(docColValueStore.getId());

        restDocColValueStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocColValueStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocColValueStore))
            )
            .andExpect(status().isOk());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
        DocColValueStore testDocColValueStore = docColValueStoreList.get(docColValueStoreList.size() - 1);
        assertThat(testDocColValueStore.getColValue()).isEqualTo(DEFAULT_COL_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateDocColValueStoreWithPatch() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();

        // Update the docColValueStore using partial update
        DocColValueStore partialUpdatedDocColValueStore = new DocColValueStore();
        partialUpdatedDocColValueStore.setId(docColValueStore.getId());

        partialUpdatedDocColValueStore.colValue(UPDATED_COL_VALUE);

        restDocColValueStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocColValueStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocColValueStore))
            )
            .andExpect(status().isOk());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
        DocColValueStore testDocColValueStore = docColValueStoreList.get(docColValueStoreList.size() - 1);
        assertThat(testDocColValueStore.getColValue()).isEqualTo(UPDATED_COL_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingDocColValueStore() throws Exception {
        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();
        docColValueStore.setId(count.incrementAndGet());

        // Create the DocColValueStore
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(docColValueStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocColValueStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docColValueStoreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocColValueStore() throws Exception {
        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();
        docColValueStore.setId(count.incrementAndGet());

        // Create the DocColValueStore
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(docColValueStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColValueStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocColValueStore() throws Exception {
        int databaseSizeBeforeUpdate = docColValueStoreRepository.findAll().size();
        docColValueStore.setId(count.incrementAndGet());

        // Create the DocColValueStore
        DocColValueStoreDTO docColValueStoreDTO = docColValueStoreMapper.toDto(docColValueStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocColValueStoreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docColValueStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocColValueStore in the database
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocColValueStore() throws Exception {
        // Initialize the database
        docColValueStoreRepository.saveAndFlush(docColValueStore);

        int databaseSizeBeforeDelete = docColValueStoreRepository.findAll().size();

        // Delete the docColValueStore
        restDocColValueStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, docColValueStore.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocColValueStore> docColValueStoreList = docColValueStoreRepository.findAll();
        assertThat(docColValueStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
