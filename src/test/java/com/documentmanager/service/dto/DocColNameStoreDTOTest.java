package com.documentmanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.documentmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocColNameStoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocColNameStoreDTO.class);
        DocColNameStoreDTO docColNameStoreDTO1 = new DocColNameStoreDTO();
        docColNameStoreDTO1.setId(1L);
        DocColNameStoreDTO docColNameStoreDTO2 = new DocColNameStoreDTO();
        assertThat(docColNameStoreDTO1).isNotEqualTo(docColNameStoreDTO2);
        docColNameStoreDTO2.setId(docColNameStoreDTO1.getId());
        assertThat(docColNameStoreDTO1).isEqualTo(docColNameStoreDTO2);
        docColNameStoreDTO2.setId(2L);
        assertThat(docColNameStoreDTO1).isNotEqualTo(docColNameStoreDTO2);
        docColNameStoreDTO1.setId(null);
        assertThat(docColNameStoreDTO1).isNotEqualTo(docColNameStoreDTO2);
    }
}
