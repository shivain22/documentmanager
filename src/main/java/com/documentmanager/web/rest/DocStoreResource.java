package com.documentmanager.web.rest;

import com.documentmanager.config.Constants;
import com.documentmanager.repository.*;
import com.documentmanager.service.*;
import com.documentmanager.service.criteria.DocStoreCriteria;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.service.impl.DocProcessor;
import com.documentmanager.web.rest.errors.BadRequestAlertException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.documentmanager.domain.DocStore}.
 */
@RestController
@RequestMapping("/api")
public class DocStoreResource {

    private final Logger log = LoggerFactory.getLogger(DocStoreResource.class);

    private static final String ENTITY_NAME = "docStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    DocStoreService docStoreService;

    @Autowired
    DocStoreQueryService docStoreQueryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DocColNameStoreRepository docColNameStoreRepository;

    @Autowired
    DocColValueStoreRepository docColValueStoreRepository;

    @Autowired
    TaskExecutor taskExecutor;

    @Autowired
    DocProcessor docProcessor;

    @Autowired
    private DocStoreAccessAuditRepository docStoreAccessAuditRepository;

    public DocStoreResource() {}

    /**
     * {@code POST  /doc-stores} : Create a new docStore.
     *
     * @param docStoreDTO the docStoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docStoreDTO, or with status {@code 400 (Bad Request)} if the docStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Secured({ Constants.ADMIN })
    @PostMapping("/doc-stores")
    public ResponseEntity<DocStoreDTO> createDocStore(@Valid @RequestBody DocStoreDTO docStoreDTO) throws URISyntaxException {
        log.debug("REST request to save DocStore : {}", docStoreDTO);
        if (docStoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new docStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            if (WorkbookFactory.create(new ByteArrayInputStream(docStoreDTO.getFileObject())) != null) {
                DocStoreDTO result = docStoreService.save(docStoreDTO);
                docProcessor.setDocStoreDTO(result);
                taskExecutor.execute(docProcessor);
                return ResponseEntity
                    .created(new URI("/api/doc-stores/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            }
        } catch (IOException e) {
            throw new BadRequestAlertException("File is not an excel file", ENTITY_NAME, "notanexcelfile");
        }
        throw new BadRequestAlertException("Internal error contact admin", ENTITY_NAME, "internalerror");
    }

    /**
     * {@code PUT  /doc-stores/:id} : Updates an existing docStore.
     *
     * @param id the id of the docStoreDTO to save.
     * @param docStoreDTO the docStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docStoreDTO,
     * or with status {@code 400 (Bad Request)} if the docStoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docStoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    /**
     * {@code PATCH  /doc-stores/:id} : Partial updates given fields of an existing docStore, field will ignore if it is null
     *
     * @param id the id of the docStoreDTO to save.
     * @param docStoreDTO the docStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docStoreDTO,
     * or with status {@code 400 (Bad Request)} if the docStoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the docStoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the docStoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    /**
     * {@code GET  /doc-stores} : get all the docStores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docStores in body.
     */
    @GetMapping("/doc-stores")
    public ResponseEntity<List<DocStoreDTO>> getAllDocStores(
        DocStoreCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocStores by criteria: {}", criteria);
        Page<DocStoreDTO> page = docStoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-stores/count} : count all the docStores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */

    /**
     * {@code GET  /doc-stores/:id} : get the "id" docStore.
     *
     * @param id the id of the docStoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docStoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-stores/{id}")
    public ResponseEntity<DocStoreDTO> getDocStore(@PathVariable Long id) {
        log.debug("REST request to get DocStore : {}", id);
        Optional<DocStoreDTO> docStoreDTO = docStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docStoreDTO);
    }

    /**
     * {@code DELETE  /doc-stores/:id} : delete the "id" docStore.
     *
     * @param id the id of the docStoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Secured({ Constants.ADMIN })
    @DeleteMapping("/doc-stores/{id}")
    public ResponseEntity<Void> deleteDocStore(@PathVariable Long id) {
        log.debug("REST request to delete DocStore : {}", id);
        docColValueStoreRepository.deleteAllByDocStoreId(id);
        docColNameStoreRepository.deleteAllByDocStoreId(id);
        docStoreAccessAuditRepository.deleteAllByDocStoreId(id);
        docStoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
