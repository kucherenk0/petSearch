import React, { FC, useState } from 'react';
import { Button, Modal, Upload } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { addPhotoToDB } from 'app/core/api';

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}

export const UploadAntd: FC = () => {
  const initialState = {
    previewVisible: false,
    previewImage: '',
    previewTitle: '',
    fileList: [],
  };

  const [error, setError] = useState(null);
  const [state, setState] = useState(initialState);
  const [resp, setResp] = useState(null);

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

  const handleSubmission = () => {
    addPhotoToDB(fileList).then(res => {
      res.data && setResp(res.data);
      res.err && setError(res.err);
    });
  };

  const handleChangeFile = ({ fileList }) => setState({ ...state, fileList });

  const { previewVisible, previewImage, fileList, previewTitle } = state;
  const uploadButton = (
    <div>
      <PlusOutlined />
      <div style={{ marginTop: 8 }}>Upload</div>
    </div>
  );

  const LoadDogPhoto = () => (
    <>
      <Upload
        listType="picture-card"
        fileList={fileList}
        onPreview={handlePreview}
        onChange={handleChangeFile}
      >
        {fileList.length >= 8 ? null : uploadButton}
      </Upload>
      <Modal
        visible={previewVisible}
        title={previewTitle}
        footer={null}
        onCancel={handleCancel}
      >
        <img alt="example" style={{ width: '100%' }} src={previewImage} />
      </Modal>
    </>
  );

  return (
    <>
      <LoadDogPhoto />
      <div>
        <Button color="success" type="primary" onClick={handleSubmission}>
          Submit
        </Button>
      </div>
      {resp && <div style={{ color: 'red', fontSize: 20 }}>{resp.toString()}</div>}
    </>
  );
};
