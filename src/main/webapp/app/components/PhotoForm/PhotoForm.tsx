import React, { FC, useEffect, useState } from 'react';
import { UploadAntd } from 'app/components';
import {
	addPhotoToDB,
	IUploadPhoto,
	IUploadPhotoResultItem,
	loadPhotoById,
} from 'app/core/api';
import PhotoResult from 'app/components/PhotoForm/PhotoResult/PhotoResult';
import { Storage } from 'react-jhipster';
import './PhotoForm.scss';
import { UploadFile } from 'antd/es/upload/interface';
import { List } from 'react-virtualized';

const INTERVAL = 1000;
const PHOTO_KEY = 'photosUploaded';

const PhotoForm: FC = props => {
	const [error, setError] = useState(null);
	const [resp, setResp] = useState<IUploadPhoto>(null);
	const [loading, setLoading] = useState(false);
	const [fileList, setFileList] = useState<UploadFile[]>([]);
	const initialPhoto = Storage.session.get(PHOTO_KEY);
	const [photos, setPhotos] = useState<IUploadPhotoResultItem[]>(initialPhoto ?? []);
	let timer;
	useEffect(() => {
		if (resp && resp?.numberOfPictures > resp?.processedPictures) {
			timer = setTimeout(() => processPhotosByID(), INTERVAL);
		} else {
			setLoading(false);
			setFileList([]);
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

	const handleSubmission = (files: UploadFile[]) => {
		setLoading(true);
		addPhotoToDB(files).then(res => {
			if (res.data) handleSetResData(res.data);
			if (res.err) {
				setError(res.err);
				setLoading(false);
			}
		});
	};

	function rowRenderer({
		key, // Unique key within array of rows
		index, // Index of row within collection
		isScrolling, // The List is currently being scrolled
		isVisible, // This row is visible within the List (eg it is not an overscanned row)
		style, // Style object to be applied to row (to position it)
	}) {
		return (
			<div key={key} style={style}>
				<PhotoResult {...photos[index]} />
			</div>
		);
	}

	return (
		<div className={'photoFormContainer'}>
			<div className={'photoFormTitle'}>
				<h3>Загрузка файлов в базу</h3>
			</div>
			<UploadAntd
				handleSubmit={handleSubmission}
				loading={loading}
				_fileList={fileList}
				setFileList={setFileList}
				maxPhotos={100}
			/>
			{Boolean(photos.length) && (
				// <List
				// 	rowRenderer={rowRenderer}
				// 	rowHeight={120}
				// 	height={400}
				// 	width={1500}
				// 	rowCount={photos.length}
				// />
				<div className={'photoListContainer'}>
					{photos.map(item => (
						<PhotoResult {...item} key={item.id} />
					))}
				</div>
			)}
		</div>
	);
};

export default PhotoForm;
