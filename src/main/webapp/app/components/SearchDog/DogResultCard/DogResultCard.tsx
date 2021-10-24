import React, { FC, useState } from 'react';
import { Button, Card, Image, Modal } from 'antd';
import { TDogResult } from '../types';
import './styles.scss';
import { Map, YMaps } from 'react-yandex-maps';

const DogResultCard: FC<TDogResult> = props => {
  const { dateOfShoot, photoAddress, filePath } = props;
  const [isOpenMap, setIsOpenMap] = useState(false);

  const onOpenModal = () => setIsOpenMap(true);

  const handleOk = () => {
    setIsOpenMap(false);
  };

  const handleCancel = () => {
    setIsOpenMap(false);
  };

  return (
    <Card title={dateOfShoot} className={'cardDog'}>
      <p>
        <Button onClick={onOpenModal}>{photoAddress}</Button>
      </p>
      <Image width={200} src={filePath} className={'imageDog'} />
      <Modal visible={isOpenMap} onOk={handleOk} onCancel={handleCancel}>
        <YMaps>
          <Map defaultState={{ center: [55.75, 37.57], zoom: 9 }} />
        </YMaps>
      </Modal>
    </Card>
  );
};

export default DogResultCard;
