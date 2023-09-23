package com.documentmanager.web.rest;

import com.documentmanager.config.Constants;
import com.documentmanager.domain.DocStore;
import com.documentmanager.domain.DocStoreAccessAudit;
import com.documentmanager.domain.User;
import com.documentmanager.repository.DocColValueStoreRepository;
import com.documentmanager.repository.DocStoreAccessAuditRepository;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.repository.UserRepository;
import com.documentmanager.security.SecurityUtils;
import com.documentmanager.service.DocColValueStoreQueryService;
import com.documentmanager.service.criteria.DocColValueStoreCriteria;
import com.documentmanager.service.dto.DocColValueStoreDTO;
import com.documentmanager.web.rest.errors.BadRequestAlertException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link com.documentmanager.domain.DocColValueStore}.
 */
@RestController
@RequestMapping("/api")
public class DocColValueStoreResource {

    private final Logger log = LoggerFactory.getLogger(DocColValueStoreResource.class);

    private static final String ENTITY_NAME = "docColValueStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocColValueStoreRepository docColValueStoreRepository;

    private final DocColValueStoreQueryService docColValueStoreQueryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DocStoreRepository docStoreRepository;

    @Autowired
    DocStoreAccessAuditRepository docStoreAccessAuditRepository;

    public DocColValueStoreResource(
        DocColValueStoreRepository docColValueStoreRepository,
        DocColValueStoreQueryService docColValueStoreQueryService
    ) {
        this.docColValueStoreRepository = docColValueStoreRepository;
        this.docColValueStoreQueryService = docColValueStoreQueryService;
    }

    /**
     * {@code GET  /doc-col-value-stores} : get all the docColValueStores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docColValueStores in body.
     */
    @Secured({ Constants.ADMIN, Constants.USER })
    @GetMapping("/doc-col-value-stores")
    public ResponseEntity<List<DocColValueStoreDTO>> getAllDocColValueStores(
        DocColValueStoreCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocColValueStores by criteria: {}", criteria);
        if (criteria.getDocStoreId().getEquals() == null) {
            throw new BadRequestAlertException("Doc ID can not be null", ENTITY_NAME, "docnotexists");
        }
        DocStore docStore = docStoreRepository.getReferenceById(criteria.getDocStoreId().getEquals());
        if (docStore == null) {
            throw new BadRequestAlertException("Doc ID provided is not available", ENTITY_NAME, "docnotexists");
        }
        User user = userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
        DocStoreAccessAudit docStoreAccessAudit = new DocStoreAccessAudit();
        docStoreAccessAudit.setDocStore(docStore);
        docStoreAccessAudit.setUser(user);
        docStoreAccessAuditRepository.save(docStoreAccessAudit);
        Page<DocColValueStoreDTO> page = docColValueStoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
