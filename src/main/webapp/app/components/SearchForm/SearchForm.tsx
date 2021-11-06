import React, { FC, useEffect, useState } from 'react';
import {
	getLastDate,
	IDogPhoto,
	ISearchParams,
	ISearchResponse,
	searchByParams,
} from 'app/core/api';
import { Button, DatePicker } from 'antd';
import {
	MOCK_SUCCES,
	optionsColor,
	optionsTail,
	types,
} from 'app/components/SearchForm/mock';
import { Input, Cascader } from '../UI';
import './SearchForm.scss';
import DogResults from './DogResults/SearchDog';
import SearchOutlined from '@ant-design/icons/lib/icons/SearchOutlined';
import { TDogResult } from 'app/components/SearchForm/DogResults/types';
import ButtonAntd from 'app/components/UI/ButtonAntd/ButtonAntd';
import { Translate } from 'react-jhipster';
import Description from 'app/components/UI/Description/Description';

const SearchForm: FC = props => {
	const initialState: ISearchParams = {
		address: '',
		dateOfLost: '',
		tail: 0,
		color: 0,
		radius: -1,
	};
	const [state, setState] = useState(initialState);
	const [isLoading, setIsLoading] = useState(false);
	const [results, setResults] = useState([]);
	const [lastDate, setLastDate] = useState('');
	let timer;
	useEffect(() => {
		timer = setInterval(() => handleSetLastDate(), 30000);
		return () => {
			clearInterval(timer);
		};
	}, []);

	const handleSetLastDate = () => {
		getLastDate()
			.then(res => setLastDate(res.data))
			.then(() => console.log(lastDate));
	};

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
		<div>
			<div className={'photoFormTitle'}>
				<h3>Поиск по параметрам</h3>
				<Description>
					<p>
						Lorem Ipsum - это текст-рыба, часто используемый в печати и
						вэб-дизайне. Lorem Ipsum является стандартной рыбой для текстов на
						латинице с начала XVI века. В то время некий безымянный печатник
						создал большую коллекцию размеров и форм шрифтов, используя Lorem
						Ipsum для распечатки образцов. Lorem Ipsum не только успешно
						пережил без заметных изменений пять ве
					</p>
				</Description>
			</div>
			{lastDate && (
				<div
					className={'lastDateBlock'}
				>{`Дата последней фотографии: ${lastDate}`}</div>
			)}
			<main className={'searchFormMain'}>
				<div className={'searchFormSider'}>
					<Input
						multiline
						value={state.address}
						placeholder="Адрес потери"
						onChange={e => handleSetField('address')(e.target.value)}
						width={100}
					/>
					<Input
						placeholder="Радиус км."
						value={state.radius < 0 ? '' : state.radius}
						onChange={e =>
							handleSetField('radius')(e.target.value.replace(/\D+/g, ''))
						}
						width={100}
					/>
					<DatePicker
						className={'petSearchDatePicker'}
						showTime={{ format: 'HH:mm' }}
						format="DD/MM/YYYY-HH:MM:00"
						onChange={handleChangeDate}
						placeholder={'Дата пропажи'}
					/>
					<Cascader
						placeholder={'Выберите породу'}
						options={types.map((item, i) => ({ label: item, value: i }))}
						onChange={() => {}}
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
					<ButtonAntd
						type={'primary'}
						className={'petSearchButton'}
						icon={<SearchOutlined />}
						onClick={handleSearch}
					>
						Найти
					</ButtonAntd>
				</div>
				<DogResults
					isLoading={isLoading}
					results={MOCK_SUCCES.classificationResult}
					limitElements={2}
				/>
			</main>
		</div>
	);
};

export default SearchForm;
