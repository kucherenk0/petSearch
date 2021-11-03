export const MOCK_SUCCES = {
  id: 1001,
  dateOfLost: '2021-10-10',
  address: 'Москва, Васильевская 4',
  classificationResult: [
    {
      filePath:
        'https://post.medicalnewstoday.com/wp-content/uploads/sites/3/2020/02/322868_1100-800x825.jpg',
      dateOfShoot: '2020-01-01',
      photoAddress: 'Москва, Покровка 38',
    },
    {
      filePath:
        'https://static01.nyt.com/images/2021/05/11/science/04TB-DOGS/04TB-DOGS-videoSixteenByNineJumbo1600.jpg',
      dateOfShoot: '2020-01-01',
      photoAddress: 'Москва, Академика Сахарова 32',
    },
    {
      filePath:
        'https://www.gannett-cdn.com/media/2021/06/03/USATODAY/usatsports/imageForEntry18-8on.jpg?width=2560',
      dateOfShoot: '2020-01-01',
      photoAddress: 'Москва, Тверскаая 10',
    },
    {
      filePath:
        'https://post.medicalnewstoday.com/wp-content/uploads/sites/3/2020/02/322868_1100-800x825.jpg',
      dateOfShoot: '2020-01-01',
      photoAddress: 'Москва, Маросейка 10',
    },
    {
      filePath:
        'https://post.medicalnewstoday.com/wp-content/uploads/sites/3/2020/02/322868_1100-800x825.jpg',
      dateOfShoot: '2020-01-01',
      photoAddress: 'Москва, Большая Полянка 28к1',
    },
    {
      filePath:
        'https://static01.nyt.com/images/2021/05/11/science/04TB-DOGS/04TB-DOGS-videoSixteenByNineJumbo1600.jpg',
      dateOfShoot: '2020-01-01',
      photoAddress: 'Москва, Шарикоподшибниковская 32',
    },
  ],
  status: 'DONE',
};

export const MOCK_IN_PROGRESS = {
  id: 1001,
  dateOfLost: '2021-10-10',
  address: 'Москва, Васильевская 4',
  classificationResult: null,
  status: 'IN_PROGRESS',
};

export const optionsTail = [
  {
    value: 0,
    label: 'Длинный',
  },
  {
    value: 1,
    label: 'Короткий',
  },
  {
    value: 2,
    label: 'Нет хвоста',
  },
];

export const optionsColor = [
  {
    value: 0,
    label: 'Темный',
  },
  {
    value: 1,
    label: 'Светлый',
  },
  {
    value: 2,
    label: 'Разноцветный',
  },
];
