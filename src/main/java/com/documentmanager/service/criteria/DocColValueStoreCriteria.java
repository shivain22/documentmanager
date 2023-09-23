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

    private LongFilter docStoreId;

    public DocColValueStoreCriteria() {}

    public DocColValueStoreCriteria(DocColValueStoreCriteria other) {
        this.docStoreId = other.docStoreId == null ? null : other.docStoreId.copy();
    }

    @Override
    public DocColValueStoreCriteria copy() {
        return new DocColValueStoreCriteria(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocColValueStoreCriteria that = (DocColValueStoreCriteria) o;
        return (Objects.equals(docStoreId, that.docStoreId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(docStoreId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocColValueStoreCriteria{" +

            (docStoreId != null ? "docStoreId=" + docStoreId + ", " : "") +

            "}";
    }
}
