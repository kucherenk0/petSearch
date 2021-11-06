import React, { FC, useState } from 'react';
import './styles.scss';
import { TDogResult } from './types';
import DogResultCard from './DogResultCard/DogResultCard';
import { Spin } from 'antd';
import ButtonAntd from 'app/components/UI/ButtonAntd/ButtonAntd';

interface IProps {
	results: TDogResult[];
	isLoading: boolean;
	limitElements?: number;
}

const DogResults: FC<IProps> = props => {
	const { results, isLoading, limitElements = 16 } = props;
	const [limit, setLimit] = useState(limitElements);

	return (
		<div>
			{results.length > 0 ? (
				<div className={'containerDog'}>
					{results.slice(0, limit).map((item, i) => (
						<DogResultCard key={i} {...item} />
					))}
					{limit < results.length && (
						<ButtonAntd
							onClick={() => {
								setLimit(prev => prev + limitElements);
							}}
						>
							Показать eще {limitElements}
						</ButtonAntd>
					)}
				</div>
			) : isLoading ? (
				<div className={'loaderContainer'}>
					<Spin size={'large'} />
				</div>
			) : (
				<h5 style={{ color: 'grey' }}>
					По вашему запросу не найдены результаты. Попробуйте изменить параметры
					поиска.
				</h5>
			)}
		</div>
	);
};

export default DogResults;
