import { COLORS, TAILS } from 'app/core/constants';

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
		label: TAILS[0],
	},
	{
		value: 1,
		label: TAILS[1],
	},
];

export const types = [
	'акита',
	'американский голый терьер',
	'бигль',
	'болонка',
	'бульдог',
	'дворовая',
	'джек-рассел-терьер',
	'йоркширский терьер',
	'корги',
	'лабрадор',
	'мопс',
	'немецкая овчарка',
	'овчарка',
	'питбуль',
	'пудель',
	'самоед',
	'такса',
	'хаски',
	'чихуахуа',
	'шпиц',
];

export const optionsColor = [
	{
		value: 0,
		label: COLORS[0],
	},
	{
		value: 1,
		label: COLORS[1],
	},
	{
		value: 2,
		label: COLORS[2],
	},
];
