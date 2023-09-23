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
import { getEntities as getDocColNameStores } from 'app/entities/doc-col-name-store/doc-col-name-store.reducer';
import { IDocColValueStore } from 'app/shared/model/doc-col-value-store.model';
import { getEntity, updateEntity, createEntity, reset } from './doc-col-value-store.reducer';

export const DocColValueStoreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const docStores = useAppSelector(state => state.docStore.entities);
  const docColNameStores = useAppSelector(state => state.docColNameStore.entities);
  const docColValueStoreEntity = useAppSelector(state => state.docColValueStore.entity);
  const loading = useAppSelector(state => state.docColValueStore.loading);
  const updating = useAppSelector(state => state.docColValueStore.updating);
  const updateSuccess = useAppSelector(state => state.docColValueStore.updateSuccess);

  const handleClose = () => {
    navigate('/doc-col-value-store');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getDocStores({}));
    dispatch(getDocColNameStores({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...docColValueStoreEntity,
      ...values,
      docStore: docStores.find(it => it.id.toString() === values.docStore.toString()),
      docColNameStore: docColNameStores.find(it => it.id.toString() === values.docColNameStore.toString()),
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
          ...docColValueStoreEntity,
          docStore: docColValueStoreEntity?.docStore?.id,
          docColNameStore: docColValueStoreEntity?.docColNameStore?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="documentmanagerApp.docColValueStore.home.createOrEditLabel" data-cy="DocColValueStoreCreateUpdateHeading">
            Create or edit a Doc Col Value Store
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
                <ValidatedField name="id" required readOnly id="doc-col-value-store-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Col Value" id="doc-col-value-store-colValue" name="colValue" data-cy="colValue" type="text" />
              <ValidatedField id="doc-col-value-store-docStore" name="docStore" data-cy="docStore" label="Doc Store" type="select" required>
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
              <ValidatedField
                id="doc-col-value-store-docColNameStore"
                name="docColNameStore"
                data-cy="docColNameStore"
                label="Doc Col Name Store"
                type="select"
                required
              >
                <option value="" key="0" />
                {docColNameStores
                  ? docColNameStores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/doc-col-value-store" replace color="info">
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

export default DocColValueStoreUpdate;
