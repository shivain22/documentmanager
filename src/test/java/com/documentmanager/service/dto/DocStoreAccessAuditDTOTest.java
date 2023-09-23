package com.documentmanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocStoreAccessAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocStoreAccessAuditDTO.class);
        DocStoreAccessAuditDTO docStoreAccessAuditDTO1 = new DocStoreAccessAuditDTO();
        docStoreAccessAuditDTO1.setId(1L);
        DocStoreAccessAuditDTO docStoreAccessAuditDTO2 = new DocStoreAccessAuditDTO();
        assertThat(docStoreAccessAuditDTO1).isNotEqualTo(docStoreAccessAuditDTO2);
        docStoreAccessAuditDTO2.setId(docStoreAccessAuditDTO1.getId());
        assertThat(docStoreAccessAuditDTO1).isEqualTo(docStoreAccessAuditDTO2);
        docStoreAccessAuditDTO2.setId(2L);
        assertThat(docStoreAccessAuditDTO1).isNotEqualTo(docStoreAccessAuditDTO2);
        docStoreAccessAuditDTO1.setId(null);
        assertThat(docStoreAccessAuditDTO1).isNotEqualTo(docStoreAccessAuditDTO2);
    }
}
