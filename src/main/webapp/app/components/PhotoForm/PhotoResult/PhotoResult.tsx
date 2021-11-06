import React, { FC } from 'react';
import { IUploadPhotoResultItem } from 'app/core/api';
import { Image, Modal } from 'antd';
import './PhotoResult.scss';
import CheckOutlined from '@ant-design/icons/lib/icons/CheckOutlined';
import List from 'antd/es/list';
import { COLORS, TAILS } from 'app/core/constants';
import { Map, Placemark, YMaps } from 'react-yandex-maps';

interface IProps {
	data: IUploadPhotoResultItem[];
}

const PhotoResult: FC<IUploadPhotoResultItem> = props => {
	const {
		date,
		color,
		id,
		address,
		cameraUid,
		downloadUrl,
		hasAnimal,
		hasDog,
		hasOwner,
		lat,
		lon,
		tail,
	} = props;

	const isMobile = window.innerWidth < 800;

	function showMap() {
		Modal.success({
			width: 564,
			content: (
				<div>
					<p>{address}</p>
					{Boolean(lat && lon) && (
						<YMaps>
							<Map
								defaultState={{ center: [+lat, +lon], zoom: 13 }}
								height={isMobile ? window.innerHeight * 0.5 : 400}
								width={isMobile ? window.innerWidth * 0.8 : 400}
							>
								<Placemark geometry={[+lat, +lon]} />
							</Map>
						</YMaps>
					)}
				</div>
			),
		});
	}

	const paramItem = (label: string, value: string | boolean) => {
		if (typeof value === 'boolean') {
			if (!value) return null;
			return (
				<p
					style={{
						display: 'flex',
						flexDirection: 'row',
						alignItems: 'center',
					}}
				>
					<span className={'photoResultItemTitle'}>{label}: </span>
					<CheckOutlined />
				</p>
			);
		}
		return (
			<p>
				<span className={'photoResultItemTitle'}>{label}: </span>
				{value}
			</p>
		);
	};

	return (
		<List.Item className={'photoResultContainer'}>
			<div className={'photoResultContainerImg photoResultContainerItem'}>
				<Image src={downloadUrl} />
			</div>
			<div className={'photoResultContainerItem'}>
				{paramItem('ID', id.toString())}
				<div onClick={showMap} style={{ cursor: 'pointer' }}>
					{paramItem('Address', address)}
				</div>
				{paramItem('Camera Uid', cameraUid)}
			</div>
			<div className={'photoResultContainerItem'}>
				{paramItem('Has dog', hasDog)}
				{paramItem('Has animal', hasAnimal)}
				{paramItem('Has owner', hasOwner)}
			</div>
			<div className={'photoResultContainerItem'}>
				{paramItem('Date', new Date(date).toLocaleString())}
				{paramItem('Color', COLORS[color - 1])}
				{paramItem('Tail', TAILS[tail - 1])}
			</div>
		</List.Item>
	);
};

export default PhotoResult;
