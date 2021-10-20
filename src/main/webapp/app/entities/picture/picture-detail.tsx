import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './picture.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PictureDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const pictureEntity = useAppSelector(state => state.picture.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pictureDetailsHeading">
          <Translate contentKey="petsearchApp.picture.detail.title">Picture</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pictureEntity.id}</dd>
          <dt>
            <span id="externalId">
              <Translate contentKey="petsearchApp.picture.externalId">External Id</Translate>
            </span>
          </dt>
          <dd>{pictureEntity.externalId}</dd>
          <dt>
            <span id="hasDog">
              <Translate contentKey="petsearchApp.picture.hasDog">Has Dog</Translate>
            </span>
          </dt>
          <dd>{pictureEntity.hasDog ? 'true' : 'false'}</dd>
          <dt>
            <span id="filePath">
              <Translate contentKey="petsearchApp.picture.filePath">File Path</Translate>
            </span>
          </dt>
          <dd>{pictureEntity.filePath}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="petsearchApp.picture.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{pictureEntity.streetAddress}</dd>
          <dt>
            <span id="cameraId">
              <Translate contentKey="petsearchApp.picture.cameraId">Camera Id</Translate>
            </span>
          </dt>
          <dd>{pictureEntity.cameraId}</dd>
          <dt>
            <span id="dateOfShoot">
              <Translate contentKey="petsearchApp.picture.dateOfShoot">Date Of Shoot</Translate>
            </span>
          </dt>
          <dd>
            {pictureEntity.dateOfShoot ? <TextFormat value={pictureEntity.dateOfShoot} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="petsearchApp.picture.user">User</Translate>
          </dt>
          <dd>{pictureEntity.user ? pictureEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/picture" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/picture/${pictureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PictureDetail;
