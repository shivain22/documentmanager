package com.documentmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocStore.class);
        DocStore docStore1 = new DocStore();
        docStore1.setId(1L);
        DocStore docStore2 = new DocStore();
        docStore2.setId(docStore1.getId());
        assertThat(docStore1).isEqualTo(docStore2);
        docStore2.setId(2L);
        assertThat(docStore1).isNotEqualTo(docStore2);
        docStore1.setId(null);
        assertThat(docStore1).isNotEqualTo(docStore2);
    }
}
