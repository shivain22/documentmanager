package com.documentmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocStoreAccessAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocStoreAccessAudit.class);
        DocStoreAccessAudit docStoreAccessAudit1 = new DocStoreAccessAudit();
        docStoreAccessAudit1.setId(1L);
        DocStoreAccessAudit docStoreAccessAudit2 = new DocStoreAccessAudit();
        docStoreAccessAudit2.setId(docStoreAccessAudit1.getId());
        assertThat(docStoreAccessAudit1).isEqualTo(docStoreAccessAudit2);
        docStoreAccessAudit2.setId(2L);
        assertThat(docStoreAccessAudit1).isNotEqualTo(docStoreAccessAudit2);
        docStoreAccessAudit1.setId(null);
        assertThat(docStoreAccessAudit1).isNotEqualTo(docStoreAccessAudit2);
    }
}
