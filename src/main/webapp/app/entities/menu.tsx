import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/doc-store">
        Doc Store
      </MenuItem>
      <MenuItem icon="asterisk" to="/doc-col-name-store">
        Doc Col Name Store
      </MenuItem>
      <MenuItem icon="asterisk" to="/doc-col-value-store">
        Doc Col Value Store
      </MenuItem>
      <MenuItem icon="asterisk" to="/doc-store-access-audit">
        Doc Store Access Audit
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
