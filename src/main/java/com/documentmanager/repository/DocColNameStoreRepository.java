package com.documentmanager.repository;

import com.documentmanager.domain.DocColNameStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocColNameStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocColNameStoreRepository extends JpaRepository<DocColNameStore, Long>, JpaSpecificationExecutor<DocColNameStore> {
    @Modifying
    @Query(value = "delete from doc_col_name_store where doc_store_id=?1", nativeQuery = true)
    void deleteAllByDocStoreId(Long docStoreId);
}
