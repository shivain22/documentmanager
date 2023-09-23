package com.documentmanager.service;

import com.documentmanager.service.dto.DocColNameStoreDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.documentmanager.domain.DocColNameStore}.
 */
public interface DocColNameStoreService {
    /**
     * Save a docColNameStore.
     *
     * @param docColNameStoreDTO the entity to save.
     * @return the persisted entity.
     */
    DocColNameStoreDTO save(DocColNameStoreDTO docColNameStoreDTO);

    /**
     * Updates a docColNameStore.
     *
     * @param docColNameStoreDTO the entity to update.
     * @return the persisted entity.
     */
    DocColNameStoreDTO update(DocColNameStoreDTO docColNameStoreDTO);

    /**
     * Partially updates a docColNameStore.
     *
     * @param docColNameStoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocColNameStoreDTO> partialUpdate(DocColNameStoreDTO docColNameStoreDTO);

    /**
     * Get all the docColNameStores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocColNameStoreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" docColNameStore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocColNameStoreDTO> findOne(Long id);

    /**
     * Delete the "id" docColNameStore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
