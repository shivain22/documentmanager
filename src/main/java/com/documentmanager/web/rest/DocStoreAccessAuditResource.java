package com.documentmanager.web.rest;

import com.documentmanager.repository.DocStoreAccessAuditRepository;
import com.documentmanager.service.DocStoreAccessAuditQueryService;
import com.documentmanager.service.criteria.DocStoreAccessAuditCriteria;
import com.documentmanager.service.dto.DocStoreAccessAuditDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link com.documentmanager.domain.DocStoreAccessAudit}.
 */
@RestController
@RequestMapping("/api")
public class DocStoreAccessAuditResource {

    private final Logger log = LoggerFactory.getLogger(DocStoreAccessAuditResource.class);

    private static final String ENTITY_NAME = "docStoreAccessAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private DocStoreAccessAuditQueryService docStoreAccessAuditQueryService;

    public DocStoreAccessAuditResource() {}

    /**
     * {@code GET  /doc-store-access-audits} : get all the docStoreAccessAudits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docStoreAccessAudits in body.
     */
    @GetMapping("/doc-store-access-audits")
    public ResponseEntity<List<DocStoreAccessAuditDTO>> getAllDocStoreAccessAudits(
        DocStoreAccessAuditCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        if (criteria.getDocStoreId().getEquals() == null) {
            throw new BadRequestAlertException("Doc id is mandatory", ENTITY_NAME, "docidnotexists");
        }
        log.debug("REST request to get DocStoreAccessAudits by criteria: {}", criteria);
        Page<DocStoreAccessAuditDTO> page = docStoreAccessAuditQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
