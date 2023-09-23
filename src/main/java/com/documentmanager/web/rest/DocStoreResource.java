package com.documentmanager.web.rest;

import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.service.DocStoreQueryService;
import com.documentmanager.service.DocStoreService;
import com.documentmanager.service.criteria.DocStoreCriteria;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    private final DocStoreService docStoreService;

    private final DocStoreRepository docStoreRepository;

    private final DocStoreQueryService docStoreQueryService;

    public DocStoreResource(
        DocStoreService docStoreService,
        DocStoreRepository docStoreRepository,
        DocStoreQueryService docStoreQueryService
    ) {
        this.docStoreService = docStoreService;
        this.docStoreRepository = docStoreRepository;
        this.docStoreQueryService = docStoreQueryService;
    }

    /**
     * {@code POST  /doc-stores} : Create a new docStore.
     *
     * @param docStoreDTO the docStoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docStoreDTO, or with status {@code 400 (Bad Request)} if the docStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-stores")
    public ResponseEntity<DocStoreDTO> createDocStore(@Valid @RequestBody DocStoreDTO docStoreDTO) throws URISyntaxException {
        log.debug("REST request to save DocStore : {}", docStoreDTO);
        if (docStoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new docStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocStoreDTO result = docStoreService.save(docStoreDTO);

        return ResponseEntity
            .created(new URI("/api/doc-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    @PutMapping("/doc-stores/{id}")
    public ResponseEntity<DocStoreDTO> updateDocStore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocStoreDTO docStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocStore : {}, {}", id, docStoreDTO);
        if (docStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocStoreDTO result = docStoreService.update(docStoreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docStoreDTO.getId().toString()))
            .body(result);
    }

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
    @PatchMapping(value = "/doc-stores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocStoreDTO> partialUpdateDocStore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocStoreDTO docStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocStore partially : {}, {}", id, docStoreDTO);
        if (docStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocStoreDTO> result = docStoreService.partialUpdate(docStoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docStoreDTO.getId().toString())
        );
    }

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
    @GetMapping("/doc-stores/count")
    public ResponseEntity<Long> countDocStores(DocStoreCriteria criteria) {
        log.debug("REST request to count DocStores by criteria: {}", criteria);
        return ResponseEntity.ok().body(docStoreQueryService.countByCriteria(criteria));
    }

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
    @DeleteMapping("/doc-stores/{id}")
    public ResponseEntity<Void> deleteDocStore(@PathVariable Long id) {
        log.debug("REST request to delete DocStore : {}", id);
        docStoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
