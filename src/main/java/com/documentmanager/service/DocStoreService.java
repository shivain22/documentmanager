package com.documentmanager.service;

import com.documentmanager.service.dto.DocStoreDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.documentmanager.domain.DocStore}.
 */
public interface DocStoreService {
    /**
     * Save a docStore.
     *
     * @param docStoreDTO the entity to save.
     * @return the persisted entity.
     */
    DocStoreDTO save(DocStoreDTO docStoreDTO);

    /**
     * Updates a docStore.
     *
     * @param docStoreDTO the entity to update.
     * @return the persisted entity.
     */
    DocStoreDTO update(DocStoreDTO docStoreDTO);

    /**
     * Partially updates a docStore.
     *
     * @param docStoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocStoreDTO> partialUpdate(DocStoreDTO docStoreDTO);

    /**
     * Get all the docStores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocStoreDTO> findAll(Pageable pageable);

    /**
     * Get all the docStores with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocStoreDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" docStore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocStoreDTO> findOne(Long id);

    /**
     * Delete the "id" docStore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void createTable(String tableName, List<String> colNames);
}
