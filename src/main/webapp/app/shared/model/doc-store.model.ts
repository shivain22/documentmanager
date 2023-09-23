import { IUser } from 'app/shared/model/user.model';

export interface IDocStore {
  id?: number;
  fileName?: string;
  fileObjectContentType?: string;
  fileObject?: string;
  process_status?: number;
  user?: IUser;
}

export const defaultValue: Readonly<IDocStore> = {};
