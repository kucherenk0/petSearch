import axios from 'axios';
import { API_URL } from 'app/core/constants';
import { message } from 'antd';
import { UploadFile } from 'antd/es/upload/interface';

export enum DogColors {
	DEFAULT = 0,
	DARK = 1,
	LIGHT = 2,
	COLORED = 3,
}

export enum DogTails {
	DEFAULT = 0,
	SHORT = 1,
	LONG = 2,
}

interface IApiResponse<T> {
	data: T;
	err: any;
}

export interface ISearchParams {
	address: string;
	dateOfLost: string;
	color: DogColors;
	tail: DogTails;
	radius: number;
}

export interface IDogPhoto {
	downloadUrl: string;
	dateOfShoot: string;
	latLong: string;
}

export interface ISearchResponse {
	id: number;
	dateOfLost: string;
	address: string;
	result: IDogPhoto[] | null;
	status: 'PENDING' | 'IN_PROGRESS' | 'DONE';
	color: DogColors;
	tail: DogTails;
}

export interface IUploadPhotoResultItem {
	id: number;
	downloadUrl: string;
	hasDog: boolean;
	hasOwner: boolean;
	hasAnimal: boolean;
	color: number;
	tail: number;
	address: string;
	lat: string;
	lon: string;
	cameraUid: string;
	date: string;
}

export interface IUploadPhoto {
	id: number;
	processedPictures: number;
	numberOfPictures: number;
	result: IUploadPhotoResultItem[];
}

export const readFiles = files => {
	return Promise.all(
		[...files].map(fileEntry => {
			const reader = new FileReader();
			reader.readAsArrayBuffer(fileEntry);
			return new Promise((resolve, reject) => {
				reader.onload = () => {
					resolve(reader.result);
				};
				reader.onerror = error => {
					reject(error);
				};
			});
		})
	);
};

export const searchByParams = async (
	params: ISearchParams
): Promise<IApiResponse<ISearchResponse>> => {
	let res, err;
	try {
		res = await axios.post(`${API_URL}/api/search`, {
			...params,
		});
	} catch (e) {
		console.log('API ERROR', e);
		message.error(`API ERROR ${e?.message ?? e}`);
		err = e;
	}
	return {
		data: res?.data,
		err,
	};
};

export const searchById = async (id: number): Promise<IApiResponse<ISearchResponse>> => {
	let res, err;
	try {
		res = await axios.get(`${API_URL}/api/search/${id}`);
	} catch (e) {
		console.log('API ERROR', e);
		message.error(`API ERROR ${e?.message ?? e}`);
		err = e;
	}
	return {
		data: res?.data,
		err,
	};
};

export const addPhotoToDB = async (
	photos: UploadFile[]
): Promise<IApiResponse<IUploadPhoto>> => {
	const formData = new FormData();
	const prep = photos.map((item: any): File => item.originFileObj ?? item);
	let res, err;
	prep.forEach(file => {
		formData.append('files', file);
	});
	try {
		res = await axios({
			method: 'post',
			url: `${API_URL}/api/pictures/upload`,
			data: formData,
			headers: {
				'Content-Type': 'multipart/form-data',
				'Access-Control-Allow-Origin': '*',
			},
		});
	} catch (e) {
		console.log('API ERROR', e);
		message.error(`API ERROR ${e?.message ?? e}`, 10);
		err = e;
	}
	return {
		data: res?.data,
		err,
	};
};

export const loadPhotoById = async (id: number): Promise<IApiResponse<IUploadPhoto>> => {
	let res, err;
	try {
		res = await axios.get(`/api/pictures/upload/${id}`);
	} catch (e) {
		console.log('API ERROR', e);
		message.error(`API ERROR ${e?.message ?? e}`);
		err = e;
	}
	return {
		data: res?.data,
		err,
	};
};

export const getLastDate = async () => {
	let res, err;
	try {
		res = await axios.get(`api/pictures/earliestDate`);
	} catch (e) {
		console.log('API ERROR', e);
		message.error(`API ERROR ${e?.message ?? e}`);
		err = e;
	}
	return {
		data: res?.data,
		err,
	};
};
