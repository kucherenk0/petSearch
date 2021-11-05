import React, { FC } from 'react';
import { Button, ButtonProps } from 'antd';
import './ButtonAntd.scss';

const ButtonAntd: FC<ButtonProps> = props => {
	return <Button {...props} className={'petSearchButton'} />;
};

export default ButtonAntd;
