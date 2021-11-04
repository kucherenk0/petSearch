import React, { FC } from 'react';
import { Input as AInput, InputProps } from 'antd';
import './Input.styles.scss';

interface Props {
	multiline?: boolean;
	onChange: (e: any) => void;
	placeholder?: string;
	width?: number;
	style?: any;
}

const Input: FC<Props> = props => {
	const { multiline, onChange, ...rest } = props;
	return multiline ? (
		<AInput className={'petSearchInput'} height={50} onChange={onChange} {...rest} />
	) : (
		<AInput.TextArea
			className={'petSearchInput'}
			rows={4}
			onChange={onChange}
			{...rest}
		/>
	);
};

export default Input;
