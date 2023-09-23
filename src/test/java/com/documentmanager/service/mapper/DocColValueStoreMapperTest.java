package com.documentmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocColValueStoreMapperTest {

    private DocColValueStoreMapper docColValueStoreMapper;

    @BeforeEach
    public void setUp() {
        docColValueStoreMapper = new DocColValueStoreMapperImpl();
    }
}
