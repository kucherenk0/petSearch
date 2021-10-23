import React, { FC, useState } from 'react';
import { Modal, Upload, Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import axios from 'axios';

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
    const formData = new FormData();
    // поправь плз)
    formData.append('dateOfLost', '2020-01-01');
    formData.append('address', 'test_addr');
    fileList.forEach(file => {
      formData.append('files[]', file);
    });

    axios
      .post('/api/search/form/', {
        body: formData,
      })
      .then(response => setResp(response))
      .catch(error => {
        console.error('Error:', error);
      });
  };

  const handleChange = ({ fileList }) => setState({ ...state, fileList });

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
        action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
        listType="picture-card"
        fileList={fileList}
        onPreview={handlePreview}
        onChange={handleChange}
      >
        {fileList.length >= 8 ? null : uploadButton}
      </Upload>
      <Modal visible={previewVisible} title={previewTitle} footer={null} onCancel={handleCancel}>
        <img alt="example" style={{ width: '100%' }} src={previewImage} />
      </Modal>
      <div>
        <Button color="success" type="primary" onClick={handleSubmission}>
          Submit
        </Button>
      </div>
      {resp && <div>{resp.toString()}</div>}
    </>
  );
};
