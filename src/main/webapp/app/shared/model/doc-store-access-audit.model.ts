import { IUser } from 'app/shared/model/user.model';
import { IDocStore } from 'app/shared/model/doc-store.model';

export interface IDocStoreAccessAudit {
  id?: number;
  user?: IUser;
  docStore?: IDocStore;
}

export const defaultValue: Readonly<IDocStoreAccessAudit> = {};
