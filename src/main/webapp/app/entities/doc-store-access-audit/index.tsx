import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocStoreAccessAudit from './doc-store-access-audit';
import DocStoreAccessAuditDetail from './doc-store-access-audit-detail';
import DocStoreAccessAuditUpdate from './doc-store-access-audit-update';
import DocStoreAccessAuditDeleteDialog from './doc-store-access-audit-delete-dialog';

const DocStoreAccessAuditRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocStoreAccessAudit />} />
    <Route path="new" element={<DocStoreAccessAuditUpdate />} />
    <Route path=":id">
      <Route index element={<DocStoreAccessAuditDetail />} />
      <Route path="edit" element={<DocStoreAccessAuditUpdate />} />
      <Route path="delete" element={<DocStoreAccessAuditDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocStoreAccessAuditRoutes;
