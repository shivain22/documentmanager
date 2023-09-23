package com.documentmanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.documentmanager.domain.DocStoreAccessAudit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocStoreAccessAuditDTO extends AbstractDTO implements Serializable {

    private Long id;

    private UserDTO user;

    private DocStoreDTO docStore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        if (!(o instanceof DocStoreAccessAuditDTO)) {
            return false;
        }

        DocStoreAccessAuditDTO docStoreAccessAuditDTO = (DocStoreAccessAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, docStoreAccessAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocStoreAccessAuditDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            ", docStore=" + getDocStore() +
            "}";
    }
}
