package com.documentmanager.repository;

import com.documentmanager.domain.DocColValueStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocColValueStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocColValueStoreRepository extends JpaRepository<DocColValueStore, Long>, JpaSpecificationExecutor<DocColValueStore> {
    @Modifying
    @Query(value = "delete from doc_col_value_store where doc_store_id=?1", nativeQuery = true)
    void deleteAllByDocStoreId(Long docStoreId);
}
