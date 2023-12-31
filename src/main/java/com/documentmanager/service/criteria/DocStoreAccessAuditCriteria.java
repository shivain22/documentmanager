package com.documentmanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.documentmanager.domain.DocStoreAccessAudit} entity. This class is used
 * in {@link com.documentmanager.web.rest.DocStoreAccessAuditResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doc-store-access-audits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocStoreAccessAuditCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userId;

    private LongFilter docStoreId;

    private Boolean distinct;

    public DocStoreAccessAuditCriteria() {}

    public DocStoreAccessAuditCriteria(DocStoreAccessAuditCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.docStoreId = other.docStoreId == null ? null : other.docStoreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocStoreAccessAuditCriteria copy() {
        return new DocStoreAccessAuditCriteria(this);
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

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
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
        final DocStoreAccessAuditCriteria that = (DocStoreAccessAuditCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(docStoreId, that.docStoreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, docStoreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocStoreAccessAuditCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (docStoreId != null ? "docStoreId=" + docStoreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
