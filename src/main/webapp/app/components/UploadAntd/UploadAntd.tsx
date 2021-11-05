import React, { FC, useState } from 'react';
import { Button, Modal, Upload } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import './UploadAndt.scss';
import ButtonAntd from 'app/components/UI/ButtonAntd/ButtonAntd';

interface IProps {
	handleSubmit: (files: File[]) => void;
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
	const { handleSubmit } = props;
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

	const handleClear = () => {
		setState(initialState);
	};

	const { previewVisible, previewImage, fileList, previewTitle } = state;
	const uploadButton = (
		<div>
			<PlusOutlined />
			<div style={{ marginTop: 8 }}>Upload</div>
		</div>
	);

	return (
		<>
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
			<div style={{ width: 300, display: 'flex', flexDirection: 'row' }}>
				<ButtonAntd
					color="success"
					type="primary"
					onClick={() => handleSubmit(fileList as File[])}
				>
					Submit
				</ButtonAntd>
				<ButtonAntd color="success" type="primary" onClick={handleClear}>
					Clear
				</ButtonAntd>
			</div>
		</>
	);
});
