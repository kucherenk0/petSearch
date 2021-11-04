import React, { FC, useState } from 'react';
import { ISearchParams } from 'app/core/api';
import { Button, DatePicker } from 'antd';
import { optionsColor, optionsTail } from 'app/components/SearchForm/mock';
import { Input, Cascader } from '../UI';
import './SearchForm.scss';
import DogResults from './DogResults/SearchDog';
import { MOCK_SUCCES } from './mock';

const SearchForm: FC = props => {
	const initialState: ISearchParams = {
		address: '',
		dateOfLost: '',
		tail: 0,
		color: 0,
		radius: 0,
	};
	const [state, setState] = useState(initialState);

	const handleSetField = (field: keyof ISearchParams) => value => {
		setState(prevState => ({
			...prevState,
			[field]: value,
		}));
	};

	const handleChangeDate = (value, dateString) =>
		handleSetField('dateOfLost')(dateString);

	return (
		<main className={'searchFormMain'}>
			<div className={'searchFormSider'}>
				<Input
					placeholder="Адрес потери"
					onChange={handleSetField('address')}
					width={100}
				/>
				<DatePicker
					className={'petSearchDatePicker'}
					showTime={{ format: 'HH:mm' }}
					format="DD/MM/YY-HH:MM:SS"
					onChange={handleChangeDate}
				/>
				<Cascader
					placeholder={'Выберите цвет'}
					options={optionsColor}
					onChange={handleSetField('color')}
				/>
				<Cascader
					placeholder={'Выберите хвост'}
					options={optionsTail}
					onChange={handleSetField('tail')}
				/>
				<Button className={'petSearchButton'} />
			</div>
			<DogResults results={MOCK_SUCCES.classificationResult} />
		</main>
	);
};

export default SearchForm;
