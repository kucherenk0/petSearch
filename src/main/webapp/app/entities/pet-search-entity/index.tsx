import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PetSearchEntity from './pet-search-entity';
import PetSearchEntityDetail from './pet-search-entity-detail';
import PetSearchEntityUpdate from './pet-search-entity-update';
import PetSearchEntityDeleteDialog from './pet-search-entity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PetSearchEntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PetSearchEntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PetSearchEntityDetail} />
      <ErrorBoundaryRoute path={match.url} component={PetSearchEntity} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PetSearchEntityDeleteDialog} />
  </>
);

export default Routes;
