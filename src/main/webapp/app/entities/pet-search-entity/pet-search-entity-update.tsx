import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './pet-search-entity.reducer';
import { IPetSearchEntity } from 'app/shared/model/pet-search-entity.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PetSearchEntityUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const petSearchEntityEntity = useAppSelector(state => state.petSearchEntity.entity);
  const loading = useAppSelector(state => state.petSearchEntity.loading);
  const updating = useAppSelector(state => state.petSearchEntity.updating);
  const updateSuccess = useAppSelector(state => state.petSearchEntity.updateSuccess);

  const handleClose = () => {
    props.history.push('/pet-search-entity' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...petSearchEntityEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.userId.toString()),
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
          status: 'PENGING',
          ...petSearchEntityEntity,
          userId: petSearchEntityEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="petsearchApp.petSearchEntity.home.createOrEditLabel" data-cy="PetSearchEntityCreateUpdateHeading">
            <Translate contentKey="petsearchApp.petSearchEntity.home.createOrEditLabel">Create or edit a PetSearchEntity</Translate>
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
                  id="pet-search-entity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('petsearchApp.petSearchEntity.dateOfLost')}
                id="pet-search-entity-dateOfLost"
                name="dateOfLost"
                data-cy="dateOfLost"
                type="date"
              />
              <ValidatedField
                label={translate('petsearchApp.petSearchEntity.status')}
                id="pet-search-entity-status"
                name="status"
                data-cy="status"
                type="select"
              >
                <option value="PENGING">{translate('petsearchApp.SearchStatus.PENGING')}</option>
                <option value="IN_PROGRESS">{translate('petsearchApp.SearchStatus.IN_PROGRESS')}</option>
                <option value="DONE">{translate('petsearchApp.SearchStatus.DONE')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('petsearchApp.petSearchEntity.adderss')}
                id="pet-search-entity-adderss"
                name="adderss"
                data-cy="adderss"
                type="text"
              />
              <ValidatedField
                id="pet-search-entity-user"
                name="userId"
                data-cy="user"
                label={translate('petsearchApp.petSearchEntity.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pet-search-entity" replace color="info">
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

export default PetSearchEntityUpdate;
