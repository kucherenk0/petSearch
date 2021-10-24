import React, { FC, useState } from 'react';
import { Button, Card, Image, Modal } from 'antd';
import { TDogResult } from '../types';
import './styles.scss';
import { Map, Placemark, YMaps } from 'react-yandex-maps';

const DogResultCard: FC<TDogResult> = props => {
  const { dateOfShoot, photoAddress, filePath } = props;

  function showMap() {
    Modal.success({
      width: 564,
      content: (
        <div>
          <p>{photoAddress}</p>
          <YMaps>
            <Map
              defaultState={{ center: [55.75, 37.57], zoom: 10 }}
              height={400}
              width={500}
            >
              <Placemark geometry={[55.75, 37.61]} />
            </Map>
          </YMaps>
        </div>
      ),
    });
  }

  return (
    <Card title={dateOfShoot} className={'cardDog'}>
      <p>
        <Button onClick={showMap}>{photoAddress}</Button>
      </p>
      <Image width={200} src={filePath} className={'imageDog'} />
    </Card>
  );
};

export default DogResultCard;
