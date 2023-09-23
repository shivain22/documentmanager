import { IDocStore } from 'app/shared/model/doc-store.model';

export interface IDocColNameStore {
  id?: number;
  colName?: string;
  docStore?: IDocStore;
}

export const defaultValue: Readonly<IDocColNameStore> = {};
