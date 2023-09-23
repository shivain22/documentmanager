import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocStore } from 'app/shared/model/doc-store.model';
import { getEntities as getDocStores } from 'app/entities/doc-store/doc-store.reducer';
import { IDocColNameStore } from 'app/shared/model/doc-col-name-store.model';
import { getEntity, updateEntity, createEntity, reset } from './doc-col-name-store.reducer';

export const DocColNameStoreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const docStores = useAppSelector(state => state.docStore.entities);
  const docColNameStoreEntity = useAppSelector(state => state.docColNameStore.entity);
  const loading = useAppSelector(state => state.docColNameStore.loading);
  const updating = useAppSelector(state => state.docColNameStore.updating);
  const updateSuccess = useAppSelector(state => state.docColNameStore.updateSuccess);

  const handleClose = () => {
    navigate('/doc-col-name-store');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getDocStores({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...docColNameStoreEntity,
      ...values,
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
          ...docColNameStoreEntity,
          docStore: docColNameStoreEntity?.docStore?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="documentmanagerApp.docColNameStore.home.createOrEditLabel" data-cy="DocColNameStoreCreateUpdateHeading">
            Create or edit a Doc Col Name Store
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
                <ValidatedField name="id" required readOnly id="doc-col-name-store-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Col Name"
                id="doc-col-name-store-colName"
                name="colName"
                data-cy="colName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 1, message: 'This field is required to be at least 1 characters.' },
                  maxLength: { value: 150, message: 'This field cannot be longer than 150 characters.' },
                }}
              />
              <ValidatedField id="doc-col-name-store-docStore" name="docStore" data-cy="docStore" label="Doc Store" type="select" required>
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/doc-col-name-store" replace color="info">
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

export default DocColNameStoreUpdate;
