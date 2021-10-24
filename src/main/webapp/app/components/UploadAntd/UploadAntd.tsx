import React, { FC, useState } from 'react';
import { Modal, Upload, Button, Image, Cascader } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import axios from 'axios';
import SearchDog from '../SearchDog/SearchDog';
import { MOCK_SUCCES, optionsColor, optionsTail } from './mock';

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
    formData.append('dateOfLost', '2020-01-01');
    formData.append('address', 'test_addr');
    fileList.forEach(file => {
      formData.append('files[]', new Blob([file]), 'filename');
    });

    axios({
      method: 'post',
      url: '/api/search/form',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
        'Access-Control-Allow-Origin': '*',
      },
    })
      .then(response => {
        setResp(response);
      })
      .catch(error => {
        setResp(error);
      });
  };

  const updateState = data => {
    setState({ ...state, ...data });
  };

  const handleChangeCascade = value => {
    updateState({ tail: value });
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
        {fileList.length >= 10 ? null : uploadButton}
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
      <Cascader
        placeholder={'Выберите цвет'}
        options={optionsColor}
        onChange={handleChangeCascade}
        style={{ marginBottom: 30 }}
      />
      <Cascader
        placeholder={'Выберите хвост'}
        options={optionsTail}
        onChange={handleChangeCascade}
        style={{ marginBottom: 30, marginLeft: 20 }}
      />
      <LoadDogPhoto />
      <div>
        <Button color="success" type="primary" onClick={handleSubmission}>
          Submit
        </Button>
      </div>
      {resp && <div style={{ color: 'red', fontSize: 20 }}>{resp.toString()}</div>}
      <SearchDog results={MOCK_SUCCES.classificationResult} />
    </>
  );
};
