import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './pet-search-entity.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PetSearchEntityDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const petSearchEntityEntity = useAppSelector(state => state.petSearchEntity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="petSearchEntityDetailsHeading">
          <Translate contentKey="petsearchApp.petSearchEntity.detail.title">PetSearchEntity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{petSearchEntityEntity.id}</dd>
          <dt>
            <span id="dateOfLost">
              <Translate contentKey="petsearchApp.petSearchEntity.dateOfLost">Date Of Lost</Translate>
            </span>
          </dt>
          <dd>
            {petSearchEntityEntity.dateOfLost ? (
              <TextFormat value={petSearchEntityEntity.dateOfLost} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="petsearchApp.petSearchEntity.status">Status</Translate>
            </span>
          </dt>
          <dd>{petSearchEntityEntity.status}</dd>
          <dt>
            <span id="adderss">
              <Translate contentKey="petsearchApp.petSearchEntity.adderss">Adderss</Translate>
            </span>
          </dt>
          <dd>{petSearchEntityEntity.adderss}</dd>
          <dt>
            <Translate contentKey="petsearchApp.petSearchEntity.user">User</Translate>
          </dt>
          <dd>{petSearchEntityEntity.user ? petSearchEntityEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pet-search-entity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pet-search-entity/${petSearchEntityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PetSearchEntityDetail;
