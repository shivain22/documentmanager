import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doc-col-name-store.reducer';

export const DocColNameStoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const docColNameStoreEntity = useAppSelector(state => state.docColNameStore.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="docColNameStoreDetailsHeading">Doc Col Name Store</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{docColNameStoreEntity.id}</dd>
          <dt>
            <span id="colName">Col Name</span>
          </dt>
          <dd>{docColNameStoreEntity.colName}</dd>
          <dt>Doc Store</dt>
          <dd>{docColNameStoreEntity.docStore ? docColNameStoreEntity.docStore.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/doc-col-name-store" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doc-col-name-store/${docColNameStoreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocColNameStoreDetail;
