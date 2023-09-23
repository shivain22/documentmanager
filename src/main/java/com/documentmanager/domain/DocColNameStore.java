package com.documentmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocColNameStore.
 */
@Entity
@Table(name = "doc_col_name_store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocColNameStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "col_name", length = 150, nullable = false)
    private String colName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private DocStore docStore;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocColNameStore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColName() {
        return this.colName;
    }

    public DocColNameStore colName(String colName) {
        this.setColName(colName);
        return this;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public DocStore getDocStore() {
        return this.docStore;
    }

    public void setDocStore(DocStore docStore) {
        this.docStore = docStore;
    }

    public DocColNameStore docStore(DocStore docStore) {
        this.setDocStore(docStore);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocColNameStore)) {
            return false;
        }
        return id != null && id.equals(((DocColNameStore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocColNameStore{" +
            "id=" + getId() +
            ", colName='" + getColName() + "'" +
            "}";
    }
}
