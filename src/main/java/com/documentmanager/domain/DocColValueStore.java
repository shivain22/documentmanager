package com.documentmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocColValueStore.
 */
@Entity
@Table(name = "doc_col_value_store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocColValueStore extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "col_value")
    private String colValue;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private DocStore docStore;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "docStore" }, allowSetters = true)
    private DocColNameStore docColNameStore;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocColValueStore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColValue() {
        return this.colValue;
    }

    public DocColValueStore colValue(String colValue) {
        this.setColValue(colValue);
        return this;
    }

    public void setColValue(String colValue) {
        this.colValue = colValue;
    }

    public DocStore getDocStore() {
        return this.docStore;
    }

    public void setDocStore(DocStore docStore) {
        this.docStore = docStore;
    }

    public DocColValueStore docStore(DocStore docStore) {
        this.setDocStore(docStore);
        return this;
    }

    public DocColNameStore getDocColNameStore() {
        return this.docColNameStore;
    }

    public void setDocColNameStore(DocColNameStore docColNameStore) {
        this.docColNameStore = docColNameStore;
    }

    public DocColValueStore docColNameStore(DocColNameStore docColNameStore) {
        this.setDocColNameStore(docColNameStore);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocColValueStore)) {
            return false;
        }
        return id != null && id.equals(((DocColValueStore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocColValueStore{" +
            "id=" + getId() +
            ", colValue='" + getColValue() + "'" +
            "}";
    }
}
