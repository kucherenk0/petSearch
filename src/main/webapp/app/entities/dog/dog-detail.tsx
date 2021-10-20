import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './dog.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DogDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const dogEntity = useAppSelector(state => state.dog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dogDetailsHeading">
          <Translate contentKey="petsearchApp.dog.detail.title">Dog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dogEntity.id}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="petsearchApp.dog.color">Color</Translate>
            </span>
          </dt>
          <dd>{dogEntity.color}</dd>
          <dt>
            <span id="dogBreed">
              <Translate contentKey="petsearchApp.dog.dogBreed">Dog Breed</Translate>
            </span>
          </dt>
          <dd>{dogEntity.dogBreed}</dd>
          <dt>
            <span id="longTail">
              <Translate contentKey="petsearchApp.dog.longTail">Long Tail</Translate>
            </span>
          </dt>
          <dd>{dogEntity.longTail ? 'true' : 'false'}</dd>
          <dt>
            <span id="hasLeash">
              <Translate contentKey="petsearchApp.dog.hasLeash">Has Leash</Translate>
            </span>
          </dt>
          <dd>{dogEntity.hasLeash ? 'true' : 'false'}</dd>
          <dt>
            <span id="coordinates">
              <Translate contentKey="petsearchApp.dog.coordinates">Coordinates</Translate>
            </span>
          </dt>
          <dd>{dogEntity.coordinates}</dd>
          <dt>
            <Translate contentKey="petsearchApp.dog.picture">Picture</Translate>
          </dt>
          <dd>{dogEntity.picture ? dogEntity.picture.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/dog" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dog/${dogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DogDetail;
