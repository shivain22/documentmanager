import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocColValueStore from './doc-col-value-store';
import DocColValueStoreDetail from './doc-col-value-store-detail';
import DocColValueStoreUpdate from './doc-col-value-store-update';
import DocColValueStoreDeleteDialog from './doc-col-value-store-delete-dialog';

const DocColValueStoreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocColValueStore />} />
    <Route path="new" element={<DocColValueStoreUpdate />} />
    <Route path=":id">
      <Route index element={<DocColValueStoreDetail />} />
      <Route path="edit" element={<DocColValueStoreUpdate />} />
      <Route path="delete" element={<DocColValueStoreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocColValueStoreRoutes;
