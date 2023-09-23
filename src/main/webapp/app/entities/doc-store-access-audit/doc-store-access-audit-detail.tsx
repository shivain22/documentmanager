import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doc-store-access-audit.reducer';

export const DocStoreAccessAuditDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const docStoreAccessAuditEntity = useAppSelector(state => state.docStoreAccessAudit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="docStoreAccessAuditDetailsHeading">Doc Store Access Audit</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{docStoreAccessAuditEntity.id}</dd>
          <dt>User</dt>
          <dd>{docStoreAccessAuditEntity.user ? docStoreAccessAuditEntity.user.login : ''}</dd>
          <dt>Doc Store</dt>
          <dd>{docStoreAccessAuditEntity.docStore ? docStoreAccessAuditEntity.docStore.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/doc-store-access-audit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doc-store-access-audit/${docStoreAccessAuditEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocStoreAccessAuditDetail;
