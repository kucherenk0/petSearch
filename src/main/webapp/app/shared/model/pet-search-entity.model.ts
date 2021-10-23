import dayjs from 'dayjs';
import { IPicture } from 'app/shared/model/picture.model';
import { IUser } from 'app/shared/model/user.model';
import { SearchStatus } from 'app/shared/model/enumerations/search-status.model';

export interface IPetSearchEntity {
  id?: number;
  dateOfLost?: string | null;
  status?: SearchStatus | null;
  adderss?: string | null;
  pictures?: IPicture[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPetSearchEntity> = {};
