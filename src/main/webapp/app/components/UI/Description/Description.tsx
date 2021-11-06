import React, { FC } from 'react';
import './Description.scss';

const Description: FC = props => {
	return <div className={'dogDescription'}>{props.children}</div>;
};

export default Description;
