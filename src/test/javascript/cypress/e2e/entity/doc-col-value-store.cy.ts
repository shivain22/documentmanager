import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('DocColValueStore e2e test', () => {
  const docColValueStorePageUrl = '/doc-col-value-store';
  const docColValueStorePageUrlPattern = new RegExp('/doc-col-value-store(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const docColValueStoreSample = {};

  let docColValueStore;
  // let docStore;
  // let docColNameStore;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/doc-stores',
      body: {"fileName":"systems Account Director","fileObject":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileObjectContentType":"unknown"},
    }).then(({ body }) => {
      docStore = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/doc-col-name-stores',
      body: {"colName":"Specialist definition"},
    }).then(({ body }) => {
      docColNameStore = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/doc-col-value-stores+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/doc-col-value-stores').as('postEntityRequest');
    cy.intercept('DELETE', '/api/doc-col-value-stores/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/doc-stores', {
      statusCode: 200,
      body: [docStore],
    });

    cy.intercept('GET', '/api/doc-col-name-stores', {
      statusCode: 200,
      body: [docColNameStore],
    });

  });
   */

  afterEach(() => {
    if (docColValueStore) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/doc-col-value-stores/${docColValueStore.id}`,
      }).then(() => {
        docColValueStore = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (docStore) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/doc-stores/${docStore.id}`,
      }).then(() => {
        docStore = undefined;
      });
    }
    if (docColNameStore) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/doc-col-name-stores/${docColNameStore.id}`,
      }).then(() => {
        docColNameStore = undefined;
      });
    }
  });
   */

  it('DocColValueStores menu should load DocColValueStores page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('doc-col-value-store');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocColValueStore').should('exist');
    cy.url().should('match', docColValueStorePageUrlPattern);
  });

  describe('DocColValueStore page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(docColValueStorePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocColValueStore page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/doc-col-value-store/new$'));
        cy.getEntityCreateUpdateHeading('DocColValueStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColValueStorePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/doc-col-value-stores',
          body: {
            ...docColValueStoreSample,
            docStore: docStore,
            docColNameStore: docColNameStore,
          },
        }).then(({ body }) => {
          docColValueStore = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/doc-col-value-stores+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/doc-col-value-stores?page=0&size=20>; rel="last",<http://localhost/api/doc-col-value-stores?page=0&size=20>; rel="first"',
              },
              body: [docColValueStore],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(docColValueStorePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(docColValueStorePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DocColValueStore page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('docColValueStore');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColValueStorePageUrlPattern);
      });

      it('edit button click should load edit DocColValueStore page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocColValueStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColValueStorePageUrlPattern);
      });

      it('edit button click should load edit DocColValueStore page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocColValueStore');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColValueStorePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DocColValueStore', () => {
        cy.intercept('GET', '/api/doc-col-value-stores/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('docColValueStore').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColValueStorePageUrlPattern);

        docColValueStore = undefined;
      });
    });
  });

  describe('new DocColValueStore page', () => {
    beforeEach(() => {
      cy.visit(`${docColValueStorePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocColValueStore');
    });

    it.skip('should create an instance of DocColValueStore', () => {
      cy.get(`[data-cy="colValue"]`).type('District').should('have.value', 'District');

      cy.get(`[data-cy="docStore"]`).select(1);
      cy.get(`[data-cy="docColNameStore"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        docColValueStore = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', docColValueStorePageUrlPattern);
    });
  });
});
