package com.documentmanager.service.impl;

import com.documentmanager.domain.DocColValueStore;
import com.documentmanager.repository.DocColValueStoreRepository;
import com.documentmanager.service.DocColValueStoreService;
import com.documentmanager.service.dto.DocColValueStoreDTO;
import com.documentmanager.service.mapper.DocColValueStoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocColValueStore}.
 */
@Service
@Transactional
public class DocColValueStoreServiceImpl implements DocColValueStoreService {

    private final Logger log = LoggerFactory.getLogger(DocColValueStoreServiceImpl.class);

    private final DocColValueStoreRepository docColValueStoreRepository;

    private final DocColValueStoreMapper docColValueStoreMapper;

    public DocColValueStoreServiceImpl(
        DocColValueStoreRepository docColValueStoreRepository,
        DocColValueStoreMapper docColValueStoreMapper
    ) {
        this.docColValueStoreRepository = docColValueStoreRepository;
        this.docColValueStoreMapper = docColValueStoreMapper;
    }

    @Override
    public DocColValueStoreDTO save(DocColValueStoreDTO docColValueStoreDTO) {
        log.debug("Request to save DocColValueStore : {}", docColValueStoreDTO);
        DocColValueStore docColValueStore = docColValueStoreMapper.toEntity(docColValueStoreDTO);
        docColValueStore = docColValueStoreRepository.save(docColValueStore);
        return docColValueStoreMapper.toDto(docColValueStore);
    }

    @Override
    public DocColValueStoreDTO update(DocColValueStoreDTO docColValueStoreDTO) {
        log.debug("Request to update DocColValueStore : {}", docColValueStoreDTO);
        DocColValueStore docColValueStore = docColValueStoreMapper.toEntity(docColValueStoreDTO);
        docColValueStore = docColValueStoreRepository.save(docColValueStore);
        return docColValueStoreMapper.toDto(docColValueStore);
    }

    @Override
    public Optional<DocColValueStoreDTO> partialUpdate(DocColValueStoreDTO docColValueStoreDTO) {
        log.debug("Request to partially update DocColValueStore : {}", docColValueStoreDTO);

        return docColValueStoreRepository
            .findById(docColValueStoreDTO.getId())
            .map(existingDocColValueStore -> {
                docColValueStoreMapper.partialUpdate(existingDocColValueStore, docColValueStoreDTO);

                return existingDocColValueStore;
            })
            .map(docColValueStoreRepository::save)
            .map(docColValueStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocColValueStoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocColValueStores");
        return docColValueStoreRepository.findAll(pageable).map(docColValueStoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocColValueStoreDTO> findOne(Long id) {
        log.debug("Request to get DocColValueStore : {}", id);
        return docColValueStoreRepository.findById(id).map(docColValueStoreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocColValueStore : {}", id);
        docColValueStoreRepository.deleteById(id);
    }
}
