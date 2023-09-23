import { IDocStore } from 'app/shared/model/doc-store.model';
import { IDocColNameStore } from 'app/shared/model/doc-col-name-store.model';

export interface IDocColValueStore {
  id?: number;
  colValue?: string | null;
  docStore?: IDocStore;
  docColNameStore?: IDocColNameStore;
}

export const defaultValue: Readonly<IDocColValueStore> = {};
