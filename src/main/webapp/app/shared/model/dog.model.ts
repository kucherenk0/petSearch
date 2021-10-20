import { IPicture } from 'app/shared/model/picture.model';

export interface IDog {
  id?: number;
  color?: string | null;
  dogBreed?: string | null;
  longTail?: boolean | null;
  hasLeash?: boolean | null;
  coordinates?: string | null;
  picture?: IPicture | null;
}

export const defaultValue: Readonly<IDog> = {
  longTail: false,
  hasLeash: false,
};
