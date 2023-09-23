package com.documentmanager.repository;

import com.documentmanager.domain.DocStore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data JPA repository for the DocStore entity.
 */
@Repository
public interface DocStoreRepository extends JpaRepository<DocStore, Long>, JpaSpecificationExecutor<DocStore> {
    @Query("select docStore from DocStore docStore where docStore.user.login = ?#{principal.preferredUsername}")
    List<DocStore> findByUserIsCurrentUser();

    default Optional<DocStore> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocStore> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocStore> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct docStore from DocStore docStore left join fetch docStore.user",
        countQuery = "select count(distinct docStore) from DocStore docStore"
    )
    Page<DocStore> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct docStore from DocStore docStore left join fetch docStore.user")
    List<DocStore> findAllWithToOneRelationships();

    @Query("select docStore from DocStore docStore left join fetch docStore.user where docStore.id =:id")
    Optional<DocStore> findOneWithToOneRelationships(@Param("id") Long id);

    @Modifying
    @Query(value = "update doc_store set process_status = ?2 where id=?1", nativeQuery = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateProcessStatus(Long docStoreId, Integer processStatus);
}
