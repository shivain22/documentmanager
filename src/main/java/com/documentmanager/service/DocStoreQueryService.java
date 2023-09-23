package com.documentmanager.service;

import com.documentmanager.domain.*; // for static metamodels
import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.service.criteria.DocStoreCriteria;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.service.mapper.DocStoreMapper;
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
 * Service for executing complex queries for {@link DocStore} entities in the database.
 * The main input is a {@link DocStoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocStoreDTO} or a {@link Page} of {@link DocStoreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocStoreQueryService extends QueryService<DocStore> {

    private final Logger log = LoggerFactory.getLogger(DocStoreQueryService.class);

    private final DocStoreRepository docStoreRepository;

    private final DocStoreMapper docStoreMapper;

    public DocStoreQueryService(DocStoreRepository docStoreRepository, DocStoreMapper docStoreMapper) {
        this.docStoreRepository = docStoreRepository;
        this.docStoreMapper = docStoreMapper;
    }

    /**
     * Return a {@link List} of {@link DocStoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocStoreDTO> findByCriteria(DocStoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocStore> specification = createSpecification(criteria);
        return docStoreMapper.toDto(docStoreRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocStoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocStoreDTO> findByCriteria(DocStoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocStore> specification = createSpecification(criteria);
        return docStoreRepository.findAll(specification, page).map(docStoreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocStoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocStore> specification = createSpecification(criteria);
        return docStoreRepository.count(specification);
    }

    /**
     * Function to convert {@link DocStoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocStore> createSpecification(DocStoreCriteria criteria) {
        Specification<DocStore> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocStore_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), DocStore_.fileName));
            }
            if (criteria.getProcess_status() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcess_status(), DocStore_.process_status));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(DocStore_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
