import React, { FC, useState } from 'react';
import { ISearchParams } from 'app/core/api';
import { Cascader, DatePicker, Input, Space } from 'antd';
import { optionsColor, optionsTail } from 'app/components/SearchForm/mock';

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
    <>
      <div>
        <Input
          placeholder="Адрес потери"
          onChange={handleSetField('address')}
          style={{ marginBottom: 20, width: 596 }}
          width={100}
        />
      </div>
      <Space direction="vertical" size={12} style={{ marginRight: 20 }}>
        <DatePicker
          showTime={{ format: 'HH:mm' }}
          format="DD/MM/YY-HH:MM:SS"
          onChange={handleChangeDate}
        />
      </Space>
      <Cascader
        placeholder={'Выберите цвет'}
        options={optionsColor}
        onChange={handleSetField('color')}
        style={{ marginBottom: 30 }}
      />
      <Cascader
        placeholder={'Выберите хвост'}
        options={optionsTail}
        onChange={handleSetField('tail')}
        style={{ marginBottom: 30, marginLeft: 20 }}
      />
    </>
  );
};

export default SearchForm;
