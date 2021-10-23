import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/picture">
      <Translate contentKey="global.menu.entities.picture" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/dog">
      <Translate contentKey="global.menu.entities.dog" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/pet-search-entity">
      <Translate contentKey="global.menu.entities.petSearchEntity" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
