package com.documentmanager.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A DocStore.
 */
@Entity
@Table(name = "doc_store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@EntityListeners(AuditingEntityListener.class)
//@Audited
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_object", nullable = false)
    private byte[] fileObject;

    @NotNull
    @Column(name = "file_object_content_type", nullable = false)
    private String fileObjectContentType;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocStore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public DocStore fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileObject() {
        return this.fileObject;
    }

    public DocStore fileObject(byte[] fileObject) {
        this.setFileObject(fileObject);
        return this;
    }

    public void setFileObject(byte[] fileObject) {
        this.fileObject = fileObject;
    }

    public String getFileObjectContentType() {
        return this.fileObjectContentType;
    }

    public DocStore fileObjectContentType(String fileObjectContentType) {
        this.fileObjectContentType = fileObjectContentType;
        return this;
    }

    public void setFileObjectContentType(String fileObjectContentType) {
        this.fileObjectContentType = fileObjectContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DocStore user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocStore)) {
            return false;
        }
        return id != null && id.equals(((DocStore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocStore{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileObject='" + getFileObject() + "'" +
            ", fileObjectContentType='" + getFileObjectContentType() + "'" +
            "}";
    }
}
