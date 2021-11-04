import React from 'react';
import { Upload, Button, Space, Input, DatePicker } from 'antd';
import { Row, Col, Alert } from 'reactstrap';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Space>
        <Input placeholder="Address" />
        <DatePicker style={{ width: '50%' }} />
      </Space>
    </Row>
  );
};

export default Home;
