import React, { FC } from 'react';
import { Input as AInput, InputProps } from 'antd';
import './Input.styles.scss';

interface Props {
	multiline?: boolean;
	onChange: (e: any) => void;
	placeholder?: string;
	width?: number;
	style?: any;
	value?: any;
}

const Input: FC<Props> = props => {
	const { multiline, onChange, style, ...rest } = props;
	return multiline ? (
		<AInput.TextArea
			className={'petSearchInput'}
			rows={3}
			onChange={onChange}
			{...rest}
		/>
	) : (
		<AInput
			className={'petSearchInput'}
			height={50}
			style={{ height: 50, ...style }}
			onChange={onChange}
			{...rest}
		/>
	);
};

export default Input;
