import React, { FC } from 'react';
import { Cascader as ACascader, CascaderProps } from 'antd';
import './Cascader.scss';

const Cascader: FC<CascaderProps> = props => {
	return <ACascader {...props} className={'petSearchCascader'} />;
};

export default Cascader;
