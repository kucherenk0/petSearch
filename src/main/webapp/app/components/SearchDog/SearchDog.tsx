import React, { FC } from 'react';
import './styles.scss';
import { TDogResult } from './types';
import DogResultCard from 'app/components/SearchDog/DogResultCard/DogResultCard';

interface IProps {
  results: TDogResult[];
}

const SearchDog: FC<IProps> = props => {
  const { results } = props;

  return (
    <div className={'containerDog'}>
      {results.length > 0 ? (
        results.map(item => <DogResultCard key={item.filePath} {...item} />)
      ) : (
        <h3>No dogs found</h3>
      )}
    </div>
  );
};

export default SearchDog;
