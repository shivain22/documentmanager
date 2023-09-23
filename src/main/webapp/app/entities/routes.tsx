import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocStore from './doc-store';
import DocColNameStore from './doc-col-name-store';
import DocColValueStore from './doc-col-value-store';
import DocStoreAccessAudit from './doc-store-access-audit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="doc-store/*" element={<DocStore />} />
        <Route path="doc-col-name-store/*" element={<DocColNameStore />} />
        <Route path="doc-col-value-store/*" element={<DocColValueStore />} />
        <Route path="doc-store-access-audit/*" element={<DocStoreAccessAudit />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
