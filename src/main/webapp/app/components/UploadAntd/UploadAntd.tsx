import React, { FC, useState } from 'react';
import { Button, Modal, Upload } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import './UploadAndt.scss';
import ButtonAntd from 'app/components/UI/ButtonAntd/ButtonAntd';
import Spin from 'antd/lib/spin';
import { message } from 'antd/es';

interface IProps {
	handleSubmit: (files: File[]) => void;
	loading?: boolean;
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
	const { handleSubmit, loading } = props;
	const initialState = {
		previewVisible: false,
		previewImage: '',
		previewTitle: '',
		fileList: [],
	};

	const [state, setState] = useState(initialState);
	let timer;

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
		setState({ ...state, fileList });
	};

	const onSubmit = () => {
		if (fileList.length > 0) {
			handleSubmit(fileList);
		} else {
			message.warn('You need to download photos', 7);
		}
		// setState(initialState);
	};

	const { previewVisible, previewImage, fileList, previewTitle } = state;
	const uploadButton = (
		<div>
			<PlusOutlined />
			<div style={{ marginTop: 8 }}>Upload</div>
		</div>
	);

	return (
		<div className={'uploadPhotoContainer'}>
			<div>
				<Upload
					className={'uploadPhotoDog'}
					listType="picture-card"
					fileList={fileList}
					onPreview={handlePreview}
					onChange={handleChangeFile}
				>
					{fileList.length >= 20 ? null : uploadButton}
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
				style={{ width: 200 }}
			>
				{loading ? <Spin className={'dogSpin'} /> : 'Submit'}
			</ButtonAntd>
		</div>
	);
});
