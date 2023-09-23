package com.documentmanager.web.rest;

import com.documentmanager.repository.DocColNameStoreRepository;
import com.documentmanager.service.DocColNameStoreQueryService;
import com.documentmanager.service.DocColNameStoreService;
import com.documentmanager.service.criteria.DocColNameStoreCriteria;
import com.documentmanager.service.dto.DocColNameStoreDTO;
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
 * REST controller for managing {@link com.documentmanager.domain.DocColNameStore}.
 */
@RestController
@RequestMapping("/api")
public class DocColNameStoreResource {

    private final Logger log = LoggerFactory.getLogger(DocColNameStoreResource.class);

    private static final String ENTITY_NAME = "docColNameStore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocColNameStoreService docColNameStoreService;

    private final DocColNameStoreRepository docColNameStoreRepository;

    private final DocColNameStoreQueryService docColNameStoreQueryService;

    public DocColNameStoreResource(
        DocColNameStoreService docColNameStoreService,
        DocColNameStoreRepository docColNameStoreRepository,
        DocColNameStoreQueryService docColNameStoreQueryService
    ) {
        this.docColNameStoreService = docColNameStoreService;
        this.docColNameStoreRepository = docColNameStoreRepository;
        this.docColNameStoreQueryService = docColNameStoreQueryService;
    }

    /**
     * {@code POST  /doc-col-name-stores} : Create a new docColNameStore.
     *
     * @param docColNameStoreDTO the docColNameStoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docColNameStoreDTO, or with status {@code 400 (Bad Request)} if the docColNameStore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-col-name-stores")
    public ResponseEntity<DocColNameStoreDTO> createDocColNameStore(@Valid @RequestBody DocColNameStoreDTO docColNameStoreDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocColNameStore : {}", docColNameStoreDTO);
        if (docColNameStoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new docColNameStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocColNameStoreDTO result = docColNameStoreService.save(docColNameStoreDTO);
        return ResponseEntity
            .created(new URI("/api/doc-col-name-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-col-name-stores/:id} : Updates an existing docColNameStore.
     *
     * @param id the id of the docColNameStoreDTO to save.
     * @param docColNameStoreDTO the docColNameStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docColNameStoreDTO,
     * or with status {@code 400 (Bad Request)} if the docColNameStoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docColNameStoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-col-name-stores/{id}")
    public ResponseEntity<DocColNameStoreDTO> updateDocColNameStore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocColNameStoreDTO docColNameStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocColNameStore : {}, {}", id, docColNameStoreDTO);
        if (docColNameStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docColNameStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docColNameStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocColNameStoreDTO result = docColNameStoreService.update(docColNameStoreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docColNameStoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-col-name-stores/:id} : Partial updates given fields of an existing docColNameStore, field will ignore if it is null
     *
     * @param id the id of the docColNameStoreDTO to save.
     * @param docColNameStoreDTO the docColNameStoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docColNameStoreDTO,
     * or with status {@code 400 (Bad Request)} if the docColNameStoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the docColNameStoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the docColNameStoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-col-name-stores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocColNameStoreDTO> partialUpdateDocColNameStore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocColNameStoreDTO docColNameStoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocColNameStore partially : {}, {}", id, docColNameStoreDTO);
        if (docColNameStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docColNameStoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docColNameStoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocColNameStoreDTO> result = docColNameStoreService.partialUpdate(docColNameStoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docColNameStoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-col-name-stores} : get all the docColNameStores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docColNameStores in body.
     */
    @GetMapping("/doc-col-name-stores")
    public ResponseEntity<List<DocColNameStoreDTO>> getAllDocColNameStores(
        DocColNameStoreCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocColNameStores by criteria: {}", criteria);
        Page<DocColNameStoreDTO> page = docColNameStoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-col-name-stores/count} : count all the docColNameStores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-col-name-stores/count")
    public ResponseEntity<Long> countDocColNameStores(DocColNameStoreCriteria criteria) {
        log.debug("REST request to count DocColNameStores by criteria: {}", criteria);
        return ResponseEntity.ok().body(docColNameStoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-col-name-stores/:id} : get the "id" docColNameStore.
     *
     * @param id the id of the docColNameStoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docColNameStoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-col-name-stores/{id}")
    public ResponseEntity<DocColNameStoreDTO> getDocColNameStore(@PathVariable Long id) {
        log.debug("REST request to get DocColNameStore : {}", id);
        Optional<DocColNameStoreDTO> docColNameStoreDTO = docColNameStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docColNameStoreDTO);
    }

    /**
     * {@code DELETE  /doc-col-name-stores/:id} : delete the "id" docColNameStore.
     *
     * @param id the id of the docColNameStoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-col-name-stores/{id}")
    public ResponseEntity<Void> deleteDocColNameStore(@PathVariable Long id) {
        log.debug("REST request to delete DocColNameStore : {}", id);
        docColNameStoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
