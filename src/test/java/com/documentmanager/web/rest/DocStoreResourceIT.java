package com.documentmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.documentmanager.IntegrationTest;
import com.documentmanager.domain.DocStore;
import com.documentmanager.domain.User;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.service.DocStoreService;
import com.documentmanager.service.criteria.DocStoreCriteria;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.service.mapper.DocStoreMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocStoreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocStoreResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_OBJECT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_OBJECT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_OBJECT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_OBJECT_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_PROCESS_STATUS = 0;
    private static final Integer UPDATED_PROCESS_STATUS = 1;
    private static final Integer SMALLER_PROCESS_STATUS = 0 - 1;

    private static final String ENTITY_API_URL = "/api/doc-stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocStoreRepository docStoreRepository;

    @Mock
    private DocStoreRepository docStoreRepositoryMock;

    @Autowired
    private DocStoreMapper docStoreMapper;

    @Mock
    private DocStoreService docStoreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocStoreMockMvc;

    private DocStore docStore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocStore createEntity(EntityManager em) {
        DocStore docStore = new DocStore()
            .fileName(DEFAULT_FILE_NAME)
            .fileObject(DEFAULT_FILE_OBJECT)
            .fileObjectContentType(DEFAULT_FILE_OBJECT_CONTENT_TYPE)
            .process_status(DEFAULT_PROCESS_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        docStore.setUser(user);
        return docStore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocStore createUpdatedEntity(EntityManager em) {
        DocStore docStore = new DocStore()
            .fileName(UPDATED_FILE_NAME)
            .fileObject(UPDATED_FILE_OBJECT)
            .fileObjectContentType(UPDATED_FILE_OBJECT_CONTENT_TYPE)
            .process_status(UPDATED_PROCESS_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        docStore.setUser(user);
        return docStore;
    }

    @BeforeEach
    public void initTest() {
        docStore = createEntity(em);
    }

    @Test
    @Transactional
    void createDocStore() throws Exception {
        int databaseSizeBeforeCreate = docStoreRepository.findAll().size();
        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);
        restDocStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeCreate + 1);
        DocStore testDocStore = docStoreList.get(docStoreList.size() - 1);
        assertThat(testDocStore.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDocStore.getFileObject()).isEqualTo(DEFAULT_FILE_OBJECT);
        assertThat(testDocStore.getFileObjectContentType()).isEqualTo(DEFAULT_FILE_OBJECT_CONTENT_TYPE);
        assertThat(testDocStore.getProcess_status()).isEqualTo(DEFAULT_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void createDocStoreWithExistingId() throws Exception {
        // Create the DocStore with an existing ID
        docStore.setId(1L);
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        int databaseSizeBeforeCreate = docStoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = docStoreRepository.findAll().size();
        // set the field null
        docStore.setFileName(null);

        // Create the DocStore, which fails.
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        restDocStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProcess_statusIsRequired() throws Exception {
        int databaseSizeBeforeTest = docStoreRepository.findAll().size();
        // set the field null
        docStore.setProcess_status(null);

        // Create the DocStore, which fails.
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        restDocStoreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocStores() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList
        restDocStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileObjectContentType").value(hasItem(DEFAULT_FILE_OBJECT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileObject").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_OBJECT))))
            .andExpect(jsonPath("$.[*].process_status").value(hasItem(DEFAULT_PROCESS_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocStoresWithEagerRelationshipsIsEnabled() throws Exception {
        when(docStoreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocStoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(docStoreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocStoresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(docStoreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocStoreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(docStoreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocStore() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get the docStore
        restDocStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, docStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docStore.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileObjectContentType").value(DEFAULT_FILE_OBJECT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileObject").value(Base64Utils.encodeToString(DEFAULT_FILE_OBJECT)))
            .andExpect(jsonPath("$.process_status").value(DEFAULT_PROCESS_STATUS));
    }

    @Test
    @Transactional
    void getDocStoresByIdFiltering() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        Long id = docStore.getId();

        defaultDocStoreShouldBeFound("id.equals=" + id);
        defaultDocStoreShouldNotBeFound("id.notEquals=" + id);

        defaultDocStoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocStoreShouldNotBeFound("id.greaterThan=" + id);

        defaultDocStoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocStoreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocStoresByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where fileName equals to DEFAULT_FILE_NAME
        defaultDocStoreShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the docStoreList where fileName equals to UPDATED_FILE_NAME
        defaultDocStoreShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocStoresByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultDocStoreShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the docStoreList where fileName equals to UPDATED_FILE_NAME
        defaultDocStoreShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocStoresByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where fileName is not null
        defaultDocStoreShouldBeFound("fileName.specified=true");

        // Get all the docStoreList where fileName is null
        defaultDocStoreShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocStoresByFileNameContainsSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where fileName contains DEFAULT_FILE_NAME
        defaultDocStoreShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the docStoreList where fileName contains UPDATED_FILE_NAME
        defaultDocStoreShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocStoresByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where fileName does not contain DEFAULT_FILE_NAME
        defaultDocStoreShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the docStoreList where fileName does not contain UPDATED_FILE_NAME
        defaultDocStoreShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsEqualToSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status equals to DEFAULT_PROCESS_STATUS
        defaultDocStoreShouldBeFound("process_status.equals=" + DEFAULT_PROCESS_STATUS);

        // Get all the docStoreList where process_status equals to UPDATED_PROCESS_STATUS
        defaultDocStoreShouldNotBeFound("process_status.equals=" + UPDATED_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsInShouldWork() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status in DEFAULT_PROCESS_STATUS or UPDATED_PROCESS_STATUS
        defaultDocStoreShouldBeFound("process_status.in=" + DEFAULT_PROCESS_STATUS + "," + UPDATED_PROCESS_STATUS);

        // Get all the docStoreList where process_status equals to UPDATED_PROCESS_STATUS
        defaultDocStoreShouldNotBeFound("process_status.in=" + UPDATED_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsNullOrNotNull() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status is not null
        defaultDocStoreShouldBeFound("process_status.specified=true");

        // Get all the docStoreList where process_status is null
        defaultDocStoreShouldNotBeFound("process_status.specified=false");
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status is greater than or equal to DEFAULT_PROCESS_STATUS
        defaultDocStoreShouldBeFound("process_status.greaterThanOrEqual=" + DEFAULT_PROCESS_STATUS);

        // Get all the docStoreList where process_status is greater than or equal to (DEFAULT_PROCESS_STATUS + 1)
        defaultDocStoreShouldNotBeFound("process_status.greaterThanOrEqual=" + (DEFAULT_PROCESS_STATUS + 1));
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status is less than or equal to DEFAULT_PROCESS_STATUS
        defaultDocStoreShouldBeFound("process_status.lessThanOrEqual=" + DEFAULT_PROCESS_STATUS);

        // Get all the docStoreList where process_status is less than or equal to SMALLER_PROCESS_STATUS
        defaultDocStoreShouldNotBeFound("process_status.lessThanOrEqual=" + SMALLER_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsLessThanSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status is less than DEFAULT_PROCESS_STATUS
        defaultDocStoreShouldNotBeFound("process_status.lessThan=" + DEFAULT_PROCESS_STATUS);

        // Get all the docStoreList where process_status is less than (DEFAULT_PROCESS_STATUS + 1)
        defaultDocStoreShouldBeFound("process_status.lessThan=" + (DEFAULT_PROCESS_STATUS + 1));
    }

    @Test
    @Transactional
    void getAllDocStoresByProcess_statusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        // Get all the docStoreList where process_status is greater than DEFAULT_PROCESS_STATUS
        defaultDocStoreShouldNotBeFound("process_status.greaterThan=" + DEFAULT_PROCESS_STATUS);

        // Get all the docStoreList where process_status is greater than SMALLER_PROCESS_STATUS
        defaultDocStoreShouldBeFound("process_status.greaterThan=" + SMALLER_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void getAllDocStoresByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            docStoreRepository.saveAndFlush(docStore);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        docStore.setUser(user);
        docStoreRepository.saveAndFlush(docStore);
        String userId = user.getId();

        // Get all the docStoreList where user equals to userId
        defaultDocStoreShouldBeFound("userId.equals=" + userId);

        // Get all the docStoreList where user equals to "invalid-id"
        defaultDocStoreShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocStoreShouldBeFound(String filter) throws Exception {
        restDocStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileObjectContentType").value(hasItem(DEFAULT_FILE_OBJECT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileObject").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_OBJECT))))
            .andExpect(jsonPath("$.[*].process_status").value(hasItem(DEFAULT_PROCESS_STATUS)));

        // Check, that the count call also returns 1
        restDocStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocStoreShouldNotBeFound(String filter) throws Exception {
        restDocStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocStore() throws Exception {
        // Get the docStore
        restDocStoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocStore() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();

        // Update the docStore
        DocStore updatedDocStore = docStoreRepository.findById(docStore.getId()).get();
        // Disconnect from session so that the updates on updatedDocStore are not directly saved in db
        em.detach(updatedDocStore);
        updatedDocStore
            .fileName(UPDATED_FILE_NAME)
            .fileObject(UPDATED_FILE_OBJECT)
            .fileObjectContentType(UPDATED_FILE_OBJECT_CONTENT_TYPE)
            .process_status(UPDATED_PROCESS_STATUS);
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(updatedDocStore);

        restDocStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
        DocStore testDocStore = docStoreList.get(docStoreList.size() - 1);
        assertThat(testDocStore.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocStore.getFileObject()).isEqualTo(UPDATED_FILE_OBJECT);
        assertThat(testDocStore.getFileObjectContentType()).isEqualTo(UPDATED_FILE_OBJECT_CONTENT_TYPE);
        assertThat(testDocStore.getProcess_status()).isEqualTo(UPDATED_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDocStore() throws Exception {
        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();
        docStore.setId(count.incrementAndGet());

        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docStoreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocStore() throws Exception {
        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();
        docStore.setId(count.incrementAndGet());

        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocStore() throws Exception {
        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();
        docStore.setId(count.incrementAndGet());

        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocStoreWithPatch() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();

        // Update the docStore using partial update
        DocStore partialUpdatedDocStore = new DocStore();
        partialUpdatedDocStore.setId(docStore.getId());

        restDocStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocStore))
            )
            .andExpect(status().isOk());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
        DocStore testDocStore = docStoreList.get(docStoreList.size() - 1);
        assertThat(testDocStore.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDocStore.getFileObject()).isEqualTo(DEFAULT_FILE_OBJECT);
        assertThat(testDocStore.getFileObjectContentType()).isEqualTo(DEFAULT_FILE_OBJECT_CONTENT_TYPE);
        assertThat(testDocStore.getProcess_status()).isEqualTo(DEFAULT_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDocStoreWithPatch() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();

        // Update the docStore using partial update
        DocStore partialUpdatedDocStore = new DocStore();
        partialUpdatedDocStore.setId(docStore.getId());

        partialUpdatedDocStore
            .fileName(UPDATED_FILE_NAME)
            .fileObject(UPDATED_FILE_OBJECT)
            .fileObjectContentType(UPDATED_FILE_OBJECT_CONTENT_TYPE)
            .process_status(UPDATED_PROCESS_STATUS);

        restDocStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocStore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocStore))
            )
            .andExpect(status().isOk());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
        DocStore testDocStore = docStoreList.get(docStoreList.size() - 1);
        assertThat(testDocStore.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocStore.getFileObject()).isEqualTo(UPDATED_FILE_OBJECT);
        assertThat(testDocStore.getFileObjectContentType()).isEqualTo(UPDATED_FILE_OBJECT_CONTENT_TYPE);
        assertThat(testDocStore.getProcess_status()).isEqualTo(UPDATED_PROCESS_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDocStore() throws Exception {
        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();
        docStore.setId(count.incrementAndGet());

        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docStoreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocStore() throws Exception {
        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();
        docStore.setId(count.incrementAndGet());

        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocStore() throws Exception {
        int databaseSizeBeforeUpdate = docStoreRepository.findAll().size();
        docStore.setId(count.incrementAndGet());

        // Create the DocStore
        DocStoreDTO docStoreDTO = docStoreMapper.toDto(docStore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocStoreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docStoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocStore in the database
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocStore() throws Exception {
        // Initialize the database
        docStoreRepository.saveAndFlush(docStore);

        int databaseSizeBeforeDelete = docStoreRepository.findAll().size();

        // Delete the docStore
        restDocStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, docStore.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocStore> docStoreList = docStoreRepository.findAll();
        assertThat(docStoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
