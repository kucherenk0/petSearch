import React, { FC, useEffect, useState } from 'react';
import { UploadAntd } from 'app/components';
import {
	addPhotoToDB,
	IUploadPhoto,
	IUploadPhotoResultItem,
	loadPhotoById,
} from 'app/core/api';
import PhotoResult from 'app/components/PhotoForm/PhotoResult/PhotoResult';
import { List } from 'antd';
import { Storage } from 'react-jhipster';
import Spin from 'antd/es/spin';
import './PhotoForm.scss';

const INTERVAL = 1000;
const PHOTO_KEY = 'photosUploaded';

const PhotoForm: FC = props => {
	const [error, setError] = useState(null);
	const [resp, setResp] = useState<IUploadPhoto>(null);
	const [loading, setLoading] = useState(false);

	const initialPhoto = Storage.session.get(PHOTO_KEY);
	const [photos, setPhotos] = useState<IUploadPhotoResultItem[]>(initialPhoto ?? []);
	let timer;
	useEffect(() => {
		if (resp && resp?.numberOfPictures > resp?.processedPictures) {
			timer = setTimeout(() => processPhotosByID(), INTERVAL);
		} else {
			setLoading(false);
		}
		return () => {
			clearTimeout(timer);
		};
	}, [resp]);

	const handleSetResData = (data: IUploadPhoto) => {
		setResp(data);
		setPhotos(data.result);
		if (data.result.length > 0) {
			Storage.session.set(PHOTO_KEY, data.result);
		}
	};

	const processPhotosByID = () => {
		if (resp && resp.id) {
			loadPhotoById(resp.id).then(res => {
				if (res.data) handleSetResData(res.data);
				if (res.err) {
					setError(res.err);
					setLoading(false);
				}
			});
		}
	};

	const handleSubmission = (fileList: File[]) => {
		setLoading(true);
		addPhotoToDB(fileList).then(res => {
			if (res.data) handleSetResData(res.data);
			if (res.err) {
				setError(res.err);
				setLoading(false);
			}
		});
	};

	return (
		<div className={'photoListContainer'}>
			<UploadAntd handleSubmit={handleSubmission} loading={loading} />
			{Boolean(photos.length) && (
				<List
					itemLayout="horizontal"
					dataSource={photos}
					renderItem={item => (
						<List.Item key={item.id}>
							<PhotoResult {...item} />
						</List.Item>
					)}
				/>
			)}
		</div>
	);
};

export default PhotoForm;
