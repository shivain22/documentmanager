package com.documentmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocStoreAccessAuditMapperTest {

    private DocStoreAccessAuditMapper docStoreAccessAuditMapper;

    @BeforeEach
    public void setUp() {
        docStoreAccessAuditMapper = new DocStoreAccessAuditMapperImpl();
    }
}
