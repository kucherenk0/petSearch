import React, { FC, useState } from 'react';
import { IDogPhoto, ISearchParams, ISearchResponse, searchByParams } from 'app/core/api';
import { Button, DatePicker } from 'antd';
import { optionsColor, optionsTail } from 'app/components/SearchForm/mock';
import { Input, Cascader } from '../UI';
import './SearchForm.scss';
import DogResults from './DogResults/SearchDog';
import { MOCK_SUCCES } from './mock';
import SearchOutlined from '@ant-design/icons/lib/icons/SearchOutlined';
import { TDogResult } from 'app/components/SearchForm/DogResults/types';

const SearchForm: FC = props => {
	const initialState: ISearchParams = {
		address: '',
		dateOfLost: '',
		tail: 0,
		color: 0,
		radius: 0,
	};
	const [state, setState] = useState(initialState);
	const [isLoading, setIsLoading] = useState(false);
	const [results, setResults] = useState([]);

	const handleSetField = (field: keyof ISearchParams) => value => {
		console.log(value);
		setState(prevState => ({
			...prevState,
			[field]: Array.isArray(value) ? value[0] : value,
		}));
	};

	const handleChangeDate = (value, dateString) =>
		handleSetField('dateOfLost')(dateString);

	const prepareResult = (response: ISearchResponse): TDogResult[] => {
		return response.result.map(item => {
			return {
				filePath: item.downloadUrl,
				dateOfShoot: item.dateOfShoot,
				photoAddress: response.address,
				coords: item.latLong.split(', ').map(num => +num),
			};
		});
	};
	const handleSearch = async () => {
		setIsLoading(true);
		const res = await searchByParams(state);
		setIsLoading(false);
		res?.data && setResults(prepareResult(res.data));
	};

	return (
		<main className={'searchFormMain'}>
			<div className={'searchFormSider'}>
				<Input
					multiline
					placeholder="Адрес потери"
					onChange={e => handleSetField('address')(e.target.value)}
					width={100}
				/>
				<DatePicker
					className={'petSearchDatePicker'}
					showTime={{ format: 'HH:mm' }}
					format="DD/MM/YYYY-HH:MM:00"
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
				<Button
					type={'primary'}
					className={'petSearchButton'}
					icon={<SearchOutlined />}
					onClick={handleSearch}
				>
					Найти
				</Button>
			</div>
			<DogResults isLoading={isLoading} results={results} />
		</main>
	);
};

export default SearchForm;
