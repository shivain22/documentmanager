package com.documentmanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.documentmanager.domain.DocColNameStore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocColNameStoreDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    private String colName;

    private DocStoreDTO docStore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public DocStoreDTO getDocStore() {
        return docStore;
    }

    public void setDocStore(DocStoreDTO docStore) {
        this.docStore = docStore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocColNameStoreDTO)) {
            return false;
        }

        DocColNameStoreDTO docColNameStoreDTO = (DocColNameStoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, docColNameStoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocColNameStoreDTO{" +
            "id=" + getId() +
            ", colName='" + getColName() + "'" +
            ", docStore=" + getDocStore() +
            "}";
    }
}
