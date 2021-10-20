import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPicture } from 'app/shared/model/picture.model';
import { getEntities as getPictures } from 'app/entities/picture/picture.reducer';
import { getEntity, updateEntity, createEntity, reset } from './dog.reducer';
import { IDog } from 'app/shared/model/dog.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DogUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const pictures = useAppSelector(state => state.picture.entities);
  const dogEntity = useAppSelector(state => state.dog.entity);
  const loading = useAppSelector(state => state.dog.loading);
  const updating = useAppSelector(state => state.dog.updating);
  const updateSuccess = useAppSelector(state => state.dog.updateSuccess);

  const handleClose = () => {
    props.history.push('/dog' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPictures({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...dogEntity,
      ...values,
      picture: pictures.find(it => it.id.toString() === values.pictureId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...dogEntity,
          pictureId: dogEntity?.picture?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="petsearchApp.dog.home.createOrEditLabel" data-cy="DogCreateUpdateHeading">
            <Translate contentKey="petsearchApp.dog.home.createOrEditLabel">Create or edit a Dog</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="dog-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('petsearchApp.dog.color')} id="dog-color" name="color" data-cy="color" type="text" />
              <ValidatedField
                label={translate('petsearchApp.dog.dogBreed')}
                id="dog-dogBreed"
                name="dogBreed"
                data-cy="dogBreed"
                type="text"
              />
              <ValidatedField
                label={translate('petsearchApp.dog.longTail')}
                id="dog-longTail"
                name="longTail"
                data-cy="longTail"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('petsearchApp.dog.hasLeash')}
                id="dog-hasLeash"
                name="hasLeash"
                data-cy="hasLeash"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('petsearchApp.dog.coordinates')}
                id="dog-coordinates"
                name="coordinates"
                data-cy="coordinates"
                type="text"
              />
              <ValidatedField
                id="dog-picture"
                name="pictureId"
                data-cy="picture"
                label={translate('petsearchApp.dog.picture')}
                type="select"
              >
                <option value="" key="0" />
                {pictures
                  ? pictures.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dog" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DogUpdate;
