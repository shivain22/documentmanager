package com.documentmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocStoreMapperTest {

    private DocStoreMapper docStoreMapper;

    @BeforeEach
    public void setUp() {
        docStoreMapper = new DocStoreMapperImpl();
    }
}
