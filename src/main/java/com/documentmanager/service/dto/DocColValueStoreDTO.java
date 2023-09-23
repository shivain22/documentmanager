package com.documentmanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.documentmanager.domain.DocColValueStore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocColValueStoreDTO implements Serializable {

    private Long id;

    private String colValue;

    private DocStoreDTO docStore;

    private DocColNameStoreDTO docColNameStore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColValue() {
        return colValue;
    }

    public void setColValue(String colValue) {
        this.colValue = colValue;
    }

    public DocStoreDTO getDocStore() {
        return docStore;
    }

    public void setDocStore(DocStoreDTO docStore) {
        this.docStore = docStore;
    }

    public DocColNameStoreDTO getDocColNameStore() {
        return docColNameStore;
    }

    public void setDocColNameStore(DocColNameStoreDTO docColNameStore) {
        this.docColNameStore = docColNameStore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocColValueStoreDTO)) {
            return false;
        }

        DocColValueStoreDTO docColValueStoreDTO = (DocColValueStoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, docColValueStoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocColValueStoreDTO{" +
            "id=" + getId() +
            ", colValue='" + getColValue() + "'" +
            ", docStore=" + getDocStore() +
            ", docColNameStore=" + getDocColNameStore() +
            "}";
    }
}
