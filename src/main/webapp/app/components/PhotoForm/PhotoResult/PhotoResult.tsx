import React, { FC } from 'react';
import { IUploadPhotoResultItem } from 'app/core/api';
import { Image } from 'antd';
import './PhotoResult.scss';
import CheckOutlined from '@ant-design/icons/lib/icons/CheckOutlined';
import List from 'antd/es/list';
import { COLORS, TAILS } from 'app/core/constants';

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
				{paramItem('Address', address)}
				{paramItem('Camera Uid', cameraUid)}
			</div>
			<div className={'photoResultContainerItem'}>
				{paramItem('Has dog', hasDog)}
				{paramItem('Has animal', hasAnimal)}
				{paramItem('Has owner', hasOwner)}
			</div>
			<div className={'photoResultContainerItem'}>
				{paramItem('Date', new Date(date).toLocaleString())}
				{paramItem('Color', COLORS[color])}
				{paramItem('Tail', TAILS[tail])}
			</div>
		</List.Item>
	);
};

export default PhotoResult;
