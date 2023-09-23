package com.documentmanager.service.impl;

import com.documentmanager.domain.DocColNameStore;
import com.documentmanager.repository.DocColNameStoreRepository;
import com.documentmanager.service.DocColNameStoreService;
import com.documentmanager.service.dto.DocColNameStoreDTO;
import com.documentmanager.service.mapper.DocColNameStoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocColNameStore}.
 */
@Service
@Transactional
public class DocColNameStoreServiceImpl implements DocColNameStoreService {

    private final Logger log = LoggerFactory.getLogger(DocColNameStoreServiceImpl.class);

    private final DocColNameStoreRepository docColNameStoreRepository;

    private final DocColNameStoreMapper docColNameStoreMapper;

    public DocColNameStoreServiceImpl(DocColNameStoreRepository docColNameStoreRepository, DocColNameStoreMapper docColNameStoreMapper) {
        this.docColNameStoreRepository = docColNameStoreRepository;
        this.docColNameStoreMapper = docColNameStoreMapper;
    }

    @Override
    public DocColNameStoreDTO save(DocColNameStoreDTO docColNameStoreDTO) {
        log.debug("Request to save DocColNameStore : {}", docColNameStoreDTO);
        DocColNameStore docColNameStore = docColNameStoreMapper.toEntity(docColNameStoreDTO);
        docColNameStore = docColNameStoreRepository.save(docColNameStore);
        return docColNameStoreMapper.toDto(docColNameStore);
    }

    @Override
    public DocColNameStoreDTO update(DocColNameStoreDTO docColNameStoreDTO) {
        log.debug("Request to update DocColNameStore : {}", docColNameStoreDTO);
        DocColNameStore docColNameStore = docColNameStoreMapper.toEntity(docColNameStoreDTO);
        docColNameStore = docColNameStoreRepository.save(docColNameStore);
        return docColNameStoreMapper.toDto(docColNameStore);
    }

    @Override
    public Optional<DocColNameStoreDTO> partialUpdate(DocColNameStoreDTO docColNameStoreDTO) {
        log.debug("Request to partially update DocColNameStore : {}", docColNameStoreDTO);

        return docColNameStoreRepository
            .findById(docColNameStoreDTO.getId())
            .map(existingDocColNameStore -> {
                docColNameStoreMapper.partialUpdate(existingDocColNameStore, docColNameStoreDTO);

                return existingDocColNameStore;
            })
            .map(docColNameStoreRepository::save)
            .map(docColNameStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocColNameStoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocColNameStores");
        return docColNameStoreRepository.findAll(pageable).map(docColNameStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocColNameStoreDTO> findOne(Long id) {
        log.debug("Request to get DocColNameStore : {}", id);
        return docColNameStoreRepository.findById(id).map(docColNameStoreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocColNameStore : {}", id);
        docColNameStoreRepository.deleteById(id);
    }
}
