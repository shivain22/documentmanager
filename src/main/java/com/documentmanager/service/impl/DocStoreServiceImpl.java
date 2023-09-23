package com.documentmanager.service.impl;

import com.documentmanager.domain.DocStore;
import com.documentmanager.repository.DocStoreRepository;
import com.documentmanager.service.DocStoreService;
import com.documentmanager.service.dto.DocStoreDTO;
import com.documentmanager.service.mapper.DocStoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocStore}.
 */
@Service
@Transactional
public class DocStoreServiceImpl implements DocStoreService {

    private final Logger log = LoggerFactory.getLogger(DocStoreServiceImpl.class);

    private final DocStoreRepository docStoreRepository;

    private final DocStoreMapper docStoreMapper;

    public DocStoreServiceImpl(DocStoreRepository docStoreRepository, DocStoreMapper docStoreMapper) {
        this.docStoreRepository = docStoreRepository;
        this.docStoreMapper = docStoreMapper;
    }

    @Override
    public DocStoreDTO save(DocStoreDTO docStoreDTO) {
        log.debug("Request to save DocStore : {}", docStoreDTO);
        DocStore docStore = docStoreMapper.toEntity(docStoreDTO);
        docStore = docStoreRepository.save(docStore);
        return docStoreMapper.toDto(docStore);
    }

    @Override
    public DocStoreDTO update(DocStoreDTO docStoreDTO) {
        log.debug("Request to update DocStore : {}", docStoreDTO);
        DocStore docStore = docStoreMapper.toEntity(docStoreDTO);
        docStore = docStoreRepository.save(docStore);
        return docStoreMapper.toDto(docStore);
    }

    @Override
    public Optional<DocStoreDTO> partialUpdate(DocStoreDTO docStoreDTO) {
        log.debug("Request to partially update DocStore : {}", docStoreDTO);

        return docStoreRepository
            .findById(docStoreDTO.getId())
            .map(existingDocStore -> {
                docStoreMapper.partialUpdate(existingDocStore, docStoreDTO);

                return existingDocStore;
            })
            .map(docStoreRepository::save)
            .map(docStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocStoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocStores");
        return docStoreRepository.findAll(pageable).map(docStoreMapper::toDto);
    }

    public Page<DocStoreDTO> findAllWithEagerRelationships(Pageable pageable) {
        return docStoreRepository.findAllWithEagerRelationships(pageable).map(docStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocStoreDTO> findOne(Long id) {
        log.debug("Request to get DocStore : {}", id);
        return docStoreRepository.findOneWithEagerRelationships(id).map(docStoreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocStore : {}", id);
        docStoreRepository.deleteById(id);
    }
}
