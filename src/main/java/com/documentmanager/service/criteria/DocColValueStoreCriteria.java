package com.documentmanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.documentmanager.domain.DocColValueStore} entity. This class is used
 * in {@link com.documentmanager.web.rest.DocColValueStoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-col-value-stores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocColValueStoreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter colValue;

    private LongFilter docStoreId;

    private LongFilter docColNameStoreId;

    private Boolean distinct;

    public DocColValueStoreCriteria() {}

    public DocColValueStoreCriteria(DocColValueStoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.colValue = other.colValue == null ? null : other.colValue.copy();
        this.docStoreId = other.docStoreId == null ? null : other.docStoreId.copy();
        this.docColNameStoreId = other.docColNameStoreId == null ? null : other.docColNameStoreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocColValueStoreCriteria copy() {
        return new DocColValueStoreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getColValue() {
        return colValue;
    }

    public StringFilter colValue() {
        if (colValue == null) {
            colValue = new StringFilter();
        }
        return colValue;
    }

    public void setColValue(StringFilter colValue) {
        this.colValue = colValue;
    }

    public LongFilter getDocStoreId() {
        return docStoreId;
    }

    public LongFilter docStoreId() {
        if (docStoreId == null) {
            docStoreId = new LongFilter();
        }
        return docStoreId;
    }

    public void setDocStoreId(LongFilter docStoreId) {
        this.docStoreId = docStoreId;
    }

    public LongFilter getDocColNameStoreId() {
        return docColNameStoreId;
    }

    public LongFilter docColNameStoreId() {
        if (docColNameStoreId == null) {
            docColNameStoreId = new LongFilter();
        }
        return docColNameStoreId;
    }

    public void setDocColNameStoreId(LongFilter docColNameStoreId) {
        this.docColNameStoreId = docColNameStoreId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocColValueStoreCriteria that = (DocColValueStoreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(colValue, that.colValue) &&
            Objects.equals(docStoreId, that.docStoreId) &&
            Objects.equals(docColNameStoreId, that.docColNameStoreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, colValue, docStoreId, docColNameStoreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocColValueStoreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (colValue != null ? "colValue=" + colValue + ", " : "") +
            (docStoreId != null ? "docStoreId=" + docStoreId + ", " : "") +
            (docColNameStoreId != null ? "docColNameStoreId=" + docColNameStoreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
