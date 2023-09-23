import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { IDocStore } from 'app/shared/model/doc-store.model';
import { getEntities as getDocStores } from 'app/entities/doc-store/doc-store.reducer';
import { IDocStoreAccessAudit } from 'app/shared/model/doc-store-access-audit.model';
import { getEntity, updateEntity, createEntity, reset } from './doc-store-access-audit.reducer';

export const DocStoreAccessAuditUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const docStores = useAppSelector(state => state.docStore.entities);
  const docStoreAccessAuditEntity = useAppSelector(state => state.docStoreAccessAudit.entity);
  const loading = useAppSelector(state => state.docStoreAccessAudit.loading);
  const updating = useAppSelector(state => state.docStoreAccessAudit.updating);
  const updateSuccess = useAppSelector(state => state.docStoreAccessAudit.updateSuccess);

  const handleClose = () => {
    navigate('/doc-store-access-audit');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getDocStores({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...docStoreAccessAuditEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      docStore: docStores.find(it => it.id.toString() === values.docStore.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...docStoreAccessAuditEntity,
          user: docStoreAccessAuditEntity?.user?.id,
          docStore: docStoreAccessAuditEntity?.docStore?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="documentmanagerApp.docStoreAccessAudit.home.createOrEditLabel" data-cy="DocStoreAccessAuditCreateUpdateHeading">
            Create or edit a Doc Store Access Audit
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="doc-store-access-audit-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField id="doc-store-access-audit-user" name="user" data-cy="user" label="User" type="select" required>
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField
                id="doc-store-access-audit-docStore"
                name="docStore"
                data-cy="docStore"
                label="Doc Store"
                type="select"
                required
              >
                <option value="" key="0" />
                {docStores
                  ? docStores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/doc-store-access-audit" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DocStoreAccessAuditUpdate;
