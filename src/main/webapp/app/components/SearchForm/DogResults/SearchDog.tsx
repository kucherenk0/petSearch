import React, { FC } from 'react';
import './styles.scss';
import { TDogResult } from './types';
import DogResultCard from './DogResultCard/DogResultCard';
import { Spin } from 'antd';

interface IProps {
	results: TDogResult[];
	isLoading: boolean;
}

const DogResults: FC<IProps> = props => {
	const { results, isLoading } = props;

	return (
		<div className={'containerDog'}>
			{results.length > 0 ? (
				results.map((item, i) => <DogResultCard key={i} {...item} />)
			) : isLoading ? (
				<div className={'loaderContainer'}>
					<Spin size={'large'} />
				</div>
			) : (
				<h3>No dogs found</h3>
			)}
		</div>
	);
};

export default DogResults;
