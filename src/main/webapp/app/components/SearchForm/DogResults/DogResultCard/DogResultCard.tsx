import React, { FC } from 'react';
import { Button, Card, Image, Modal, Popover } from 'antd';
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
      <p style={{ height: 60 }}>
        <Popover content={photoAddress}>
          <Button onClick={showMap} className={'cardDogAddress'}>
            <div className={'cardDogAddressText'}>{photoAddress}</div>
          </Button>
        </Popover>
      </p>
      <div className={'cardDogImgContainer'}>
        <Image
          alt={photoAddress}
          width={200}
          src={filePath}
          className={'cardDogImageDog'}
        />
      </div>
    </Card>
  );
};

export default DogResultCard;
