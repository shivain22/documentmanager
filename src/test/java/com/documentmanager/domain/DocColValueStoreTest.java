package com.documentmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocColValueStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocColValueStore.class);
        DocColValueStore docColValueStore1 = new DocColValueStore();
        docColValueStore1.setId(1L);
        DocColValueStore docColValueStore2 = new DocColValueStore();
        docColValueStore2.setId(docColValueStore1.getId());
        assertThat(docColValueStore1).isEqualTo(docColValueStore2);
        docColValueStore2.setId(2L);
        assertThat(docColValueStore1).isNotEqualTo(docColValueStore2);
        docColValueStore1.setId(null);
        assertThat(docColValueStore1).isNotEqualTo(docColValueStore2);
    }
}
