import axios from 'axios';
import { API_URL } from 'app/core/constants';

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
		err = e;
	}
	return {
		data: res.data,
		err,
	};
};

export const searchById = async (id: number): Promise<IApiResponse<ISearchResponse>> => {
	let res, err;
	try {
		res = await axios.get(`${API_URL}/api/search/${id}`);
	} catch (e) {
		console.log('API ERROR', e);
		err = e;
	}
	return {
		data: res.data,
		err,
	};
};

export const addPhotoToDB = async (
	photos: File[]
): Promise<IApiResponse<IUploadPhoto>> => {
	const formData = new FormData();
	let res, err;
	photos.forEach(file => {
		formData.append('files', new Blob([file]), 'filename');
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
		err = e;
	}
	return {
		data: res.data,
		err,
	};
};

export const loadPhotoById = async (id: number): Promise<IApiResponse<IUploadPhoto>> => {
	let res, err;
	try {
		res = await axios.get(`/api/pictures/upload/${id}`);
	} catch (e) {
		console.log('API ERROR', e);
		err = e;
	}
	return {
		data: res.data,
		err,
	};
};
