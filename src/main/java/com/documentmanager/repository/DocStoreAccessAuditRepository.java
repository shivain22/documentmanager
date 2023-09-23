package com.documentmanager.repository;

import com.documentmanager.domain.DocStoreAccessAudit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocStoreAccessAudit entity.
 */
@Repository
public interface DocStoreAccessAuditRepository
    extends JpaRepository<DocStoreAccessAudit, Long>, JpaSpecificationExecutor<DocStoreAccessAudit> {
    @Query(
        "select docStoreAccessAudit from DocStoreAccessAudit docStoreAccessAudit where docStoreAccessAudit.user.login = ?#{principal.preferredUsername}"
    )
    List<DocStoreAccessAudit> findByUserIsCurrentUser();

    default Optional<DocStoreAccessAudit> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocStoreAccessAudit> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocStoreAccessAudit> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct docStoreAccessAudit from DocStoreAccessAudit docStoreAccessAudit left join fetch docStoreAccessAudit.user",
        countQuery = "select count(distinct docStoreAccessAudit) from DocStoreAccessAudit docStoreAccessAudit"
    )
    Page<DocStoreAccessAudit> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct docStoreAccessAudit from DocStoreAccessAudit docStoreAccessAudit left join fetch docStoreAccessAudit.user")
    List<DocStoreAccessAudit> findAllWithToOneRelationships();

    @Query(
        "select docStoreAccessAudit from DocStoreAccessAudit docStoreAccessAudit left join fetch docStoreAccessAudit.user where docStoreAccessAudit.id =:id"
    )
    Optional<DocStoreAccessAudit> findOneWithToOneRelationships(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from doc_store_access_audit where doc_store_id=?1", nativeQuery = true)
    void deleteAllByDocStoreId(Long docStoreId);
}
