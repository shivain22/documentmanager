package com.documentmanager.service;

import com.documentmanager.domain.*; // for static metamodels
import com.documentmanager.domain.DocStoreAccessAudit;
import com.documentmanager.repository.DocStoreAccessAuditRepository;
import com.documentmanager.service.criteria.DocStoreAccessAuditCriteria;
import com.documentmanager.service.dto.DocStoreAccessAuditDTO;
import com.documentmanager.service.mapper.DocStoreAccessAuditMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DocStoreAccessAudit} entities in the database.
 * The main input is a {@link DocStoreAccessAuditCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocStoreAccessAuditDTO} or a {@link Page} of {@link DocStoreAccessAuditDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocStoreAccessAuditQueryService extends QueryService<DocStoreAccessAudit> {

    private final Logger log = LoggerFactory.getLogger(DocStoreAccessAuditQueryService.class);

    private final DocStoreAccessAuditRepository docStoreAccessAuditRepository;

    private final DocStoreAccessAuditMapper docStoreAccessAuditMapper;

    public DocStoreAccessAuditQueryService(
        DocStoreAccessAuditRepository docStoreAccessAuditRepository,
        DocStoreAccessAuditMapper docStoreAccessAuditMapper
    ) {
        this.docStoreAccessAuditRepository = docStoreAccessAuditRepository;
        this.docStoreAccessAuditMapper = docStoreAccessAuditMapper;
    }

    /**
     * Return a {@link List} of {@link DocStoreAccessAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocStoreAccessAuditDTO> findByCriteria(DocStoreAccessAuditCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocStoreAccessAudit> specification = createSpecification(criteria);
        return docStoreAccessAuditMapper.toDto(docStoreAccessAuditRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocStoreAccessAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocStoreAccessAuditDTO> findByCriteria(DocStoreAccessAuditCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocStoreAccessAudit> specification = createSpecification(criteria);
        return docStoreAccessAuditRepository.findAll(specification, page).map(docStoreAccessAuditMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocStoreAccessAuditCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocStoreAccessAudit> specification = createSpecification(criteria);
        return docStoreAccessAuditRepository.count(specification);
    }

    /**
     * Function to convert {@link DocStoreAccessAuditCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocStoreAccessAudit> createSpecification(DocStoreAccessAuditCriteria criteria) {
        Specification<DocStoreAccessAudit> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocStoreAccessAudit_.id));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(DocStoreAccessAudit_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getDocStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocStoreId(),
                            root -> root.join(DocStoreAccessAudit_.docStore, JoinType.LEFT).get(DocStore_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
