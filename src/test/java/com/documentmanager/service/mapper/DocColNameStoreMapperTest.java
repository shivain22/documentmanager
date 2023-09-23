package com.documentmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocColNameStoreMapperTest {

    private DocColNameStoreMapper docColNameStoreMapper;

    @BeforeEach
    public void setUp() {
        docColNameStoreMapper = new DocColNameStoreMapperImpl();
    }
}
