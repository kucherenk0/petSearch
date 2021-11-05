import React, { FC } from 'react';
import { IUploadPhotoResultItem } from 'app/core/api';
import { Image } from 'antd';
import './PhotoResult.scss';
import CheckOutlined from '@ant-design/icons/lib/icons/CheckOutlined';
import List from 'antd/es/list';

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
				<p>
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
				{paramItem('Address', address)}
				{paramItem('Camera Uid', cameraUid)}
			</div>
			<div className={'photoResultContainerItem'}>
				{paramItem('Has dog', hasDog)}
				{paramItem('Has animal', hasAnimal)}
				{paramItem('Has owner', hasOwner)}
			</div>
		</List.Item>
	);
};

export default PhotoResult;
