import React, { FC } from 'react';
import { Button, Card, Image, Modal, Popover } from 'antd';
import { TDogResult } from '../types';
import './styles.scss';
import { Map, Placemark, YMaps } from 'react-yandex-maps';

const DogResultCard: FC<TDogResult> = props => {
	const { dateOfShoot, photoAddress, filePath, coords } = props;
	const isMobile = window.innerWidth < 800;
	console.log(coords);
	function showMap() {
		Modal.success({
			width: 564,
			content: (
				<div>
					<p>{photoAddress}</p>
					{Boolean(coords && coords.length === 2) && (
						<YMaps>
							<Map
								defaultState={{ center: coords, zoom: 10 }}
								height={isMobile ? window.innerHeight * 0.5 : 400}
								width={isMobile ? window.innerWidth * 0.8 : 400}
							>
								<Placemark geometry={coords} />
							</Map>
						</YMaps>
					)}
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
