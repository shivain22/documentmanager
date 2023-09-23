import docStore from 'app/entities/doc-store/doc-store.reducer';
import docColNameStore from 'app/entities/doc-col-name-store/doc-col-name-store.reducer';
import docColValueStore from 'app/entities/doc-col-value-store/doc-col-value-store.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  docStore,
  docColNameStore,
  docColValueStore,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
