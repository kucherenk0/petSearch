import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dog from './dog';
import DogDetail from './dog-detail';
import DogUpdate from './dog-update';
import DogDeleteDialog from './dog-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DogDetail} />
      <ErrorBoundaryRoute path={match.url} component={Dog} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DogDeleteDialog} />
  </>
);

export default Routes;
