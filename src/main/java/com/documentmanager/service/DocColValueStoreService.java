package com.documentmanager.service;

import com.documentmanager.service.dto.DocColValueStoreDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.documentmanager.domain.DocColValueStore}.
 */
public interface DocColValueStoreService {
    /**
     * Save a docColValueStore.
     *
     * @param docColValueStoreDTO the entity to save.
     * @return the persisted entity.
     */
    DocColValueStoreDTO save(DocColValueStoreDTO docColValueStoreDTO);

    /**
     * Updates a docColValueStore.
     *
     * @param docColValueStoreDTO the entity to update.
     * @return the persisted entity.
     */
    DocColValueStoreDTO update(DocColValueStoreDTO docColValueStoreDTO);

    /**
     * Partially updates a docColValueStore.
     *
     * @param docColValueStoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocColValueStoreDTO> partialUpdate(DocColValueStoreDTO docColValueStoreDTO);

    /**
     * Get all the docColValueStores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocColValueStoreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" docColValueStore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocColValueStoreDTO> findOne(Long id);

    /**
     * Delete the "id" docColValueStore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
