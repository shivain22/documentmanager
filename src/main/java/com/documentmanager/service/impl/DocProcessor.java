package com.documentmanager.service.impl;

import com.documentmanager.domain.DocStore;
import com.documentmanager.service.dto.DocStoreDTO;
import org.springframework.stereotype.Service;

@Service
public class DocProcessor implements Runnable {

    DocStoreDTO docStoreDTO;

    public DocStoreDTO getDocStoreDTO() {
        return docStoreDTO;
    }

    public void setDocStoreDTO(DocStoreDTO docStoreDTO) {
        this.docStoreDTO = docStoreDTO;
    }

    @Override
    public void run() {
        docStoreDTO.getFileObject();
        docStoreDTO.getFileName();
    }
}
