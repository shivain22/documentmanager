package com.documentmanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocStoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocStoreDTO.class);
        DocStoreDTO docStoreDTO1 = new DocStoreDTO();
        docStoreDTO1.setId(1L);
        DocStoreDTO docStoreDTO2 = new DocStoreDTO();
        assertThat(docStoreDTO1).isNotEqualTo(docStoreDTO2);
        docStoreDTO2.setId(docStoreDTO1.getId());
        assertThat(docStoreDTO1).isEqualTo(docStoreDTO2);
        docStoreDTO2.setId(2L);
        assertThat(docStoreDTO1).isNotEqualTo(docStoreDTO2);
        docStoreDTO1.setId(null);
        assertThat(docStoreDTO1).isNotEqualTo(docStoreDTO2);
    }
}
