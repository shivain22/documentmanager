package com.documentmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocColNameStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocColNameStore.class);
        DocColNameStore docColNameStore1 = new DocColNameStore();
        docColNameStore1.setId(1L);
        DocColNameStore docColNameStore2 = new DocColNameStore();
        docColNameStore2.setId(docColNameStore1.getId());
        assertThat(docColNameStore1).isEqualTo(docColNameStore2);
        docColNameStore2.setId(2L);
        assertThat(docColNameStore1).isNotEqualTo(docColNameStore2);
        docColNameStore1.setId(null);
        assertThat(docColNameStore1).isNotEqualTo(docColNameStore2);
    }
}
