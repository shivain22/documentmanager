package com.documentmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.documentmanager.IntegrationTest;
import com.documentmanager.domain.DocStore;
import com.documentmanager.domain.DocStoreAccessAudit;
import com.documentmanager.domain.User;
import com.documentmanager.repository.DocStoreAccessAuditRepository;
import com.documentmanager.service.dto.DocStoreAccessAuditDTO;
import com.documentmanager.service.mapper.DocStoreAccessAuditMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DocStoreAccessAuditResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocStoreAccessAuditResourceIT {

    private static final String ENTITY_API_URL = "/api/doc-store-access-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocStoreAccessAuditRepository docStoreAccessAuditRepository;

    @Mock
    private DocStoreAccessAuditRepository docStoreAccessAuditRepositoryMock;

    @Autowired
    private DocStoreAccessAuditMapper docStoreAccessAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocStoreAccessAuditMockMvc;

    private DocStoreAccessAudit docStoreAccessAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocStoreAccessAudit createEntity(EntityManager em) {
        DocStoreAccessAudit docStoreAccessAudit = new DocStoreAccessAudit();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        docStoreAccessAudit.setUser(user);
        // Add required entity
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docStore = DocStoreResourceIT.createEntity(em);
            em.persist(docStore);
            em.flush();
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        docStoreAccessAudit.setDocStore(docStore);
        return docStoreAccessAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocStoreAccessAudit createUpdatedEntity(EntityManager em) {
        DocStoreAccessAudit docStoreAccessAudit = new DocStoreAccessAudit();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        docStoreAccessAudit.setUser(user);
        // Add required entity
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docStore = DocStoreResourceIT.createUpdatedEntity(em);
            em.persist(docStore);
            em.flush();
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        docStoreAccessAudit.setDocStore(docStore);
        return docStoreAccessAudit;
    }

    @BeforeEach
    public void initTest() {
        docStoreAccessAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeCreate = docStoreAccessAuditRepository.findAll().size();
        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);
        restDocStoreAccessAuditMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeCreate + 1);
        DocStoreAccessAudit testDocStoreAccessAudit = docStoreAccessAuditList.get(docStoreAccessAuditList.size() - 1);
    }

    @Test
    @Transactional
    void createDocStoreAccessAuditWithExistingId() throws Exception {
        // Create the DocStoreAccessAudit with an existing ID
        docStoreAccessAudit.setId(1L);
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        int databaseSizeBeforeCreate = docStoreAccessAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocStoreAccessAuditMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocStoreAccessAudits() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        // Get all the docStoreAccessAuditList
        restDocStoreAccessAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docStoreAccessAudit.getId().intValue())));
    }

    @Test
    @Transactional
    void getDocStoreAccessAudit() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        // Get the docStoreAccessAudit
        restDocStoreAccessAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, docStoreAccessAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docStoreAccessAudit.getId().intValue()));
    }

    @Test
    @Transactional
    void getDocStoreAccessAuditsByIdFiltering() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        Long id = docStoreAccessAudit.getId();

        defaultDocStoreAccessAuditShouldBeFound("id.equals=" + id);
        defaultDocStoreAccessAuditShouldNotBeFound("id.notEquals=" + id);

        defaultDocStoreAccessAuditShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocStoreAccessAuditShouldNotBeFound("id.greaterThan=" + id);

        defaultDocStoreAccessAuditShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocStoreAccessAuditShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocStoreAccessAuditsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        docStoreAccessAudit.setUser(user);
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);
        String userId = user.getId();

        // Get all the docStoreAccessAuditList where user equals to userId
        defaultDocStoreAccessAuditShouldBeFound("userId.equals=" + userId);

        // Get all the docStoreAccessAuditList where user equals to "invalid-id"
        defaultDocStoreAccessAuditShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllDocStoreAccessAuditsByDocStoreIsEqualToSomething() throws Exception {
        DocStore docStore;
        if (TestUtil.findAll(em, DocStore.class).isEmpty()) {
            docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);
            docStore = DocStoreResourceIT.createEntity(em);
        } else {
            docStore = TestUtil.findAll(em, DocStore.class).get(0);
        }
        em.persist(docStore);
        em.flush();
        docStoreAccessAudit.setDocStore(docStore);
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);
        Long docStoreId = docStore.getId();

        // Get all the docStoreAccessAuditList where docStore equals to docStoreId
        defaultDocStoreAccessAuditShouldBeFound("docStoreId.equals=" + docStoreId);

        // Get all the docStoreAccessAuditList where docStore equals to (docStoreId + 1)
        defaultDocStoreAccessAuditShouldNotBeFound("docStoreId.equals=" + (docStoreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocStoreAccessAuditShouldBeFound(String filter) throws Exception {
        restDocStoreAccessAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docStoreAccessAudit.getId().intValue())));

        // Check, that the count call also returns 1
        restDocStoreAccessAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocStoreAccessAuditShouldNotBeFound(String filter) throws Exception {
        restDocStoreAccessAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocStoreAccessAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocStoreAccessAudit() throws Exception {
        // Get the docStoreAccessAudit
        restDocStoreAccessAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocStoreAccessAudit() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();

        // Update the docStoreAccessAudit
        DocStoreAccessAudit updatedDocStoreAccessAudit = docStoreAccessAuditRepository.findById(docStoreAccessAudit.getId()).get();
        // Disconnect from session so that the updates on updatedDocStoreAccessAudit are not directly saved in db
        em.detach(updatedDocStoreAccessAudit);
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(updatedDocStoreAccessAudit);

        restDocStoreAccessAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docStoreAccessAuditDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
        DocStoreAccessAudit testDocStoreAccessAudit = docStoreAccessAuditList.get(docStoreAccessAuditList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();
        docStoreAccessAudit.setId(count.incrementAndGet());

        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocStoreAccessAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docStoreAccessAuditDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();
        docStoreAccessAudit.setId(count.incrementAndGet());

        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreAccessAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();
        docStoreAccessAudit.setId(count.incrementAndGet());

        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreAccessAuditMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocStoreAccessAuditWithPatch() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();

        // Update the docStoreAccessAudit using partial update
        DocStoreAccessAudit partialUpdatedDocStoreAccessAudit = new DocStoreAccessAudit();
        partialUpdatedDocStoreAccessAudit.setId(docStoreAccessAudit.getId());

        restDocStoreAccessAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocStoreAccessAudit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocStoreAccessAudit))
            )
            .andExpect(status().isOk());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
        DocStoreAccessAudit testDocStoreAccessAudit = docStoreAccessAuditList.get(docStoreAccessAuditList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateDocStoreAccessAuditWithPatch() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();

        // Update the docStoreAccessAudit using partial update
        DocStoreAccessAudit partialUpdatedDocStoreAccessAudit = new DocStoreAccessAudit();
        partialUpdatedDocStoreAccessAudit.setId(docStoreAccessAudit.getId());

        restDocStoreAccessAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocStoreAccessAudit.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocStoreAccessAudit))
            )
            .andExpect(status().isOk());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
        DocStoreAccessAudit testDocStoreAccessAudit = docStoreAccessAuditList.get(docStoreAccessAuditList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();
        docStoreAccessAudit.setId(count.incrementAndGet());

        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocStoreAccessAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docStoreAccessAuditDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();
        docStoreAccessAudit.setId(count.incrementAndGet());

        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreAccessAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocStoreAccessAudit() throws Exception {
        int databaseSizeBeforeUpdate = docStoreAccessAuditRepository.findAll().size();
        docStoreAccessAudit.setId(count.incrementAndGet());

        // Create the DocStoreAccessAudit
        DocStoreAccessAuditDTO docStoreAccessAuditDTO = docStoreAccessAuditMapper.toDto(docStoreAccessAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreAccessAuditMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docStoreAccessAuditDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocStoreAccessAudit in the database
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocStoreAccessAudit() throws Exception {
        // Initialize the database
        docStoreAccessAuditRepository.saveAndFlush(docStoreAccessAudit);

        int databaseSizeBeforeDelete = docStoreAccessAuditRepository.findAll().size();

        // Delete the docStoreAccessAudit
        restDocStoreAccessAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, docStoreAccessAudit.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocStoreAccessAudit> docStoreAccessAuditList = docStoreAccessAuditRepository.findAll();
        assertThat(docStoreAccessAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
