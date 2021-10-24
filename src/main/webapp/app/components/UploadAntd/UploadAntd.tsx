import React, { FC, useState } from 'react';
import { Modal, Upload, Button, Image, Cascader, Space, DatePicker, Input } from 'antd';
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
    tail: '',
    color: '',
    dateOfLost: '',
    address: '',
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
    formData.append('dateOfLost', state.dateOfLost || '2020-01-01');
    formData.append('address', state.address || 'test_addr');
    formData.append('tail', state.tail);
    formData.append('color', state.color);

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
    console.log(data);
    setState({ ...state, ...data });
  };

  const handleChangeDate = (value, dateString) => {
    console.log(dateString);
    updateState({ dateOfLost: dateString });
  };

  const handleChangeTail = value => {
    updateState({ tail: value });
  };

  const handleChangeColor = value => {
    updateState({ color: value });
  };

  const handleChangeFile = ({ fileList }) => setState({ ...state, fileList });

  const handleChangeAdress = e => {
    console.log(e.target.value);
    updateState({ address: e.target.value });
  };

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
      <div>
        <Input
          placeholder="Адрес потери"
          onChange={handleChangeAdress}
          style={{ marginBottom: 20, width: 596 }}
          width={100}
        />
      </div>
      <Space direction="vertical" size={12} style={{ marginRight: 20 }}>
        <DatePicker
          showTime={{ format: 'HH:mm' }}
          format="DD/MM/YY-HH:MM:SS"
          onChange={handleChangeDate}
        />
      </Space>
      <Cascader
        placeholder={'Выберите цвет'}
        options={optionsColor}
        onChange={handleChangeColor}
        style={{ marginBottom: 30 }}
      />
      <Cascader
        placeholder={'Выберите хвост'}
        options={optionsTail}
        onChange={handleChangeTail}
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
