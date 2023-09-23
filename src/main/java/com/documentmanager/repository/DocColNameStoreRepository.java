package com.documentmanager.repository;

import com.documentmanager.domain.DocColNameStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocColNameStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocColNameStoreRepository extends JpaRepository<DocColNameStore, Long>, JpaSpecificationExecutor<DocColNameStore> {}
