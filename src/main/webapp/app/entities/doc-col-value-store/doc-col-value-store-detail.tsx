import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doc-col-value-store.reducer';

export const DocColValueStoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const docColValueStoreEntity = useAppSelector(state => state.docColValueStore.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="docColValueStoreDetailsHeading">Doc Col Value Store</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{docColValueStoreEntity.id}</dd>
          <dt>
            <span id="colValue">Col Value</span>
          </dt>
          <dd>{docColValueStoreEntity.colValue}</dd>
          <dt>Doc Store</dt>
          <dd>{docColValueStoreEntity.docStore ? docColValueStoreEntity.docStore.id : ''}</dd>
          <dt>Doc Col Name Store</dt>
          <dd>{docColValueStoreEntity.docColNameStore ? docColValueStoreEntity.docColNameStore.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/doc-col-value-store" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doc-col-value-store/${docColValueStoreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocColValueStoreDetail;
