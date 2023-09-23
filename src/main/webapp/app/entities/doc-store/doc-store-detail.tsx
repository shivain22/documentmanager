import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doc-store.reducer';

export const DocStoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const docStoreEntity = useAppSelector(state => state.docStore.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="docStoreDetailsHeading">Doc Store</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{docStoreEntity.id}</dd>
          <dt>
            <span id="fileName">File Name</span>
          </dt>
          <dd>{docStoreEntity.fileName}</dd>
          <dt>
            <span id="fileObject">File Object</span>
          </dt>
          <dd>
            {docStoreEntity.fileObject ? (
              <div>
                {docStoreEntity.fileObjectContentType ? (
                  <a onClick={openFile(docStoreEntity.fileObjectContentType, docStoreEntity.fileObject)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {docStoreEntity.fileObjectContentType}, {byteSize(docStoreEntity.fileObject)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>User</dt>
          <dd>{docStoreEntity.user ? docStoreEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/doc-store" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doc-store/${docStoreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocStoreDetail;
