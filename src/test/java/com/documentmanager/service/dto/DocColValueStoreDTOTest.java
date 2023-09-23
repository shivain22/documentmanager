package com.documentmanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocColValueStoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocColValueStoreDTO.class);
        DocColValueStoreDTO docColValueStoreDTO1 = new DocColValueStoreDTO();
        docColValueStoreDTO1.setId(1L);
        DocColValueStoreDTO docColValueStoreDTO2 = new DocColValueStoreDTO();
        assertThat(docColValueStoreDTO1).isNotEqualTo(docColValueStoreDTO2);
        docColValueStoreDTO2.setId(docColValueStoreDTO1.getId());
        assertThat(docColValueStoreDTO1).isEqualTo(docColValueStoreDTO2);
        docColValueStoreDTO2.setId(2L);
        assertThat(docColValueStoreDTO1).isNotEqualTo(docColValueStoreDTO2);
        docColValueStoreDTO1.setId(null);
        assertThat(docColValueStoreDTO1).isNotEqualTo(docColValueStoreDTO2);
    }
}
