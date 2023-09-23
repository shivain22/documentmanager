import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocColNameStore from './doc-col-name-store';
import DocColNameStoreDetail from './doc-col-name-store-detail';
import DocColNameStoreUpdate from './doc-col-name-store-update';
import DocColNameStoreDeleteDialog from './doc-col-name-store-delete-dialog';

const DocColNameStoreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocColNameStore />} />
    <Route path="new" element={<DocColNameStoreUpdate />} />
    <Route path=":id">
      <Route index element={<DocColNameStoreDetail />} />
      <Route path="edit" element={<DocColNameStoreUpdate />} />
      <Route path="delete" element={<DocColNameStoreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocColNameStoreRoutes;
