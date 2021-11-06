import React, { FC, useEffect, useState } from 'react';
import { Button, Modal, Upload } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import './UploadAndt.scss';
import ButtonAntd from 'app/components/UI/ButtonAntd/ButtonAntd';
import Spin from 'antd/lib/spin';
import { message } from 'antd/es';
import { UploadFile } from 'antd/es/upload/interface';

interface IProps {
	handleSubmit: (files: UploadFile[]) => void;
	loading?: boolean;
	_fileList: UploadFile[];
	setFileList: (a: UploadFile[]) => void;
	maxPhotos?: number;
}

function getBase64(file) {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.readAsDataURL(file);
		reader.onload = () => resolve(reader.result);
		reader.onerror = error => reject(error);
	});
}

export const UploadAntd: FC<IProps> = React.memo(props => {
	const { handleSubmit, loading, _fileList, setFileList, maxPhotos } = props;
	const initialState = {
		previewVisible: false,
		previewImage: '',
		previewTitle: '',
	};

	const [state, setState] = useState(initialState);
	let timer;

	const handleClearForm = () => setState(initialState);

	const handleCancel = () => setState({ ...state, previewVisible: false });
	const handlePreview = async file => {
		if (!file.url && !file.preview) {
			file.preview = await getBase64(file.originFileObj);
		}

		setState({
			...state,
			previewImage: file.url || file.preview,
			previewVisible: true,
			previewTitle: file.name || file.url.substring(file.url.lastIndexOf('/') + 1),
		});
	};

	const handleChangeFile = ({ fileList }) => {
		setFileList(fileList);
	};

	const onSubmit = () => {
		if (_fileList.length > 0) {
			handleSubmit(_fileList);
		} else {
			message.warn('Необходимо загрузить фотографии', 7);
		}
		// setState(initialState);
	};

	const { previewVisible, previewImage, previewTitle } = state;
	const uploadButton = (
		<div>
			<PlusOutlined />
			<div style={{ marginTop: 8 }}>Загрузить</div>
		</div>
	);

	return (
		<div className={'uploadPhotoContainer'}>
			<div>
				<Upload
					className={'uploadPhotoDog'}
					listType="picture-card"
					fileList={_fileList}
					onPreview={handlePreview}
					onChange={handleChangeFile}
				>
					{_fileList.length >= (maxPhotos ?? 20) ? null : uploadButton}
				</Upload>
				<Modal
					visible={previewVisible}
					title={previewTitle}
					footer={null}
					onCancel={handleCancel}
				>
					<img alt="example" style={{ width: '100%' }} src={previewImage} />
				</Modal>
			</div>
			<ButtonAntd
				color="success"
				type="primary"
				onClick={onSubmit}
				disabled={loading}
				style={{ width: 200, marginTop: 20 }}
			>
				{loading ? <Spin className={'dogSpin'} /> : 'Отправить'}
			</ButtonAntd>
		</div>
	);
});
