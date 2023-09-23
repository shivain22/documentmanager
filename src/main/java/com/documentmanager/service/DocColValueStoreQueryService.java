package com.documentmanager.service;

import com.documentmanager.domain.*; // for static metamodels
import com.documentmanager.domain.DocColValueStore;
import com.documentmanager.repository.DocColValueStoreRepository;
import com.documentmanager.service.criteria.DocColValueStoreCriteria;
import com.documentmanager.service.dto.DocColValueStoreDTO;
import com.documentmanager.service.mapper.DocColValueStoreMapper;
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
 * Service for executing complex queries for {@link DocColValueStore} entities in the database.
 * The main input is a {@link DocColValueStoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocColValueStoreDTO} or a {@link Page} of {@link DocColValueStoreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocColValueStoreQueryService extends QueryService<DocColValueStore> {

    private final Logger log = LoggerFactory.getLogger(DocColValueStoreQueryService.class);

    private final DocColValueStoreRepository docColValueStoreRepository;

    private final DocColValueStoreMapper docColValueStoreMapper;

    public DocColValueStoreQueryService(
        DocColValueStoreRepository docColValueStoreRepository,
        DocColValueStoreMapper docColValueStoreMapper
    ) {
        this.docColValueStoreRepository = docColValueStoreRepository;
        this.docColValueStoreMapper = docColValueStoreMapper;
    }

    /**
     * Return a {@link List} of {@link DocColValueStoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocColValueStoreDTO> findByCriteria(DocColValueStoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocColValueStore> specification = createSpecification(criteria);
        return docColValueStoreMapper.toDto(docColValueStoreRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocColValueStoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocColValueStoreDTO> findByCriteria(DocColValueStoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocColValueStore> specification = createSpecification(criteria);
        return docColValueStoreRepository.findAll(specification, page).map(docColValueStoreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocColValueStoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocColValueStore> specification = createSpecification(criteria);
        return docColValueStoreRepository.count(specification);
    }

    /**
     * Function to convert {@link DocColValueStoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocColValueStore> createSpecification(DocColValueStoreCriteria criteria) {
        Specification<DocColValueStore> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocColValueStore_.id));
            }
            if (criteria.getColValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColValue(), DocColValueStore_.colValue));
            }
            if (criteria.getDocStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocStoreId(),
                            root -> root.join(DocColValueStore_.docStore, JoinType.LEFT).get(DocStore_.id)
                        )
                    );
            }
            if (criteria.getDocColNameStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocColNameStoreId(),
                            root -> root.join(DocColValueStore_.docColNameStore, JoinType.LEFT).get(DocColNameStore_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
