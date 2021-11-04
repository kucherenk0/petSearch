import React, { FC } from 'react';
import './styles.scss';
import { TDogResult } from './types';
import DogResultCard from './DogResultCard/DogResultCard';

interface IProps {
  results: TDogResult[];
}

const DogResults: FC<IProps> = props => {
  const { results } = props;

  return (
    <div className={'containerDog'}>
      {results.length > 0 ? (
        results.map((item, i) => <DogResultCard key={i} {...item} />)
      ) : (
        <h3>No dogs found</h3>
      )}
    </div>
  );
};

export default DogResults;
