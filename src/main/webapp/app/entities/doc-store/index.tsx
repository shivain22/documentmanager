import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocStore from './doc-store';
import DocStoreDetail from './doc-store-detail';
import DocStoreUpdate from './doc-store-update';
import DocStoreDeleteDialog from './doc-store-delete-dialog';

const DocStoreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocStore />} />
    <Route path="new" element={<DocStoreUpdate />} />
    <Route path=":id">
      <Route index element={<DocStoreDetail />} />
      <Route path="edit" element={<DocStoreUpdate />} />
      <Route path="delete" element={<DocStoreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocStoreRoutes;
