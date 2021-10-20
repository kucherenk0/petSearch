import dayjs from 'dayjs';
import { IDog } from 'app/shared/model/dog.model';
import { IUser } from 'app/shared/model/user.model';

export interface IPicture {
  id?: number;
  externalId?: string;
  hasDog?: boolean | null;
  filePath?: string;
  streetAddress?: string | null;
  cameraId?: string | null;
  dateOfShoot?: string | null;
  dogs?: IDog[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPicture> = {
  hasDog: false,
};
