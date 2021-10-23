import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Picture from './picture';
import Dog from './dog';
import PetSearchEntity from './pet-search-entity';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}picture`} component={Picture} />
      <ErrorBoundaryRoute path={`${match.url}dog`} component={Dog} />
      <ErrorBoundaryRoute path={`${match.url}pet-search-entity`} component={PetSearchEntity} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
