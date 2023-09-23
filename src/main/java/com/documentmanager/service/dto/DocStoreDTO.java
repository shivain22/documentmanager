package com.documentmanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.documentmanager.domain.DocStore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocStoreDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String fileName;

    @Lob
    private byte[] fileObject;

    private String fileObjectContentType;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer processStatus;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileObject() {
        return fileObject;
    }

    public void setFileObject(byte[] fileObject) {
        this.fileObject = fileObject;
    }

    public String getFileObjectContentType() {
        return fileObjectContentType;
    }

    public void setFileObjectContentType(String fileObjectContentType) {
        this.fileObjectContentType = fileObjectContentType;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocStoreDTO)) {
            return false;
        }

        DocStoreDTO docStoreDTO = (DocStoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, docStoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocStoreDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileObject='" + getFileObject() + "'" +
            ", process_status=" + getProcessStatus() +
            ", user=" + getUser() +
            "}";
    }
}
