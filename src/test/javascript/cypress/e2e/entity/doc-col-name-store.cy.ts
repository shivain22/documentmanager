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

describe('DocColNameStore e2e test', () => {
  const docColNameStorePageUrl = '/doc-col-name-store';
  const docColNameStorePageUrlPattern = new RegExp('/doc-col-name-store(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const docColNameStoreSample = {"colName":"Auto models red"};

  let docColNameStore;
  // let docStore;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/doc-stores',
      body: {"fileName":"Principal integrate Plastic","fileObject":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileObjectContentType":"unknown","process_status":1},
    }).then(({ body }) => {
      docStore = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/doc-col-name-stores+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/doc-col-name-stores').as('postEntityRequest');
    cy.intercept('DELETE', '/api/doc-col-name-stores/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/doc-stores', {
      statusCode: 200,
      body: [docStore],
    });

  });
   */

  afterEach(() => {
    if (docColNameStore) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/doc-col-name-stores/${docColNameStore.id}`,
      }).then(() => {
        docColNameStore = undefined;
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
  });
   */

  it('DocColNameStores menu should load DocColNameStores page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('doc-col-name-store');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocColNameStore').should('exist');
    cy.url().should('match', docColNameStorePageUrlPattern);
  });

  describe('DocColNameStore page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(docColNameStorePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocColNameStore page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/doc-col-name-store/new$'));
        cy.getEntityCreateUpdateHeading('DocColNameStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColNameStorePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/doc-col-name-stores',
          body: {
            ...docColNameStoreSample,
            docStore: docStore,
          },
        }).then(({ body }) => {
          docColNameStore = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/doc-col-name-stores+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/doc-col-name-stores?page=0&size=20>; rel="last",<http://localhost/api/doc-col-name-stores?page=0&size=20>; rel="first"',
              },
              body: [docColNameStore],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(docColNameStorePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(docColNameStorePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DocColNameStore page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('docColNameStore');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColNameStorePageUrlPattern);
      });

      it('edit button click should load edit DocColNameStore page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocColNameStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColNameStorePageUrlPattern);
      });

      it('edit button click should load edit DocColNameStore page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocColNameStore');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColNameStorePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DocColNameStore', () => {
        cy.intercept('GET', '/api/doc-col-name-stores/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('docColNameStore').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docColNameStorePageUrlPattern);

        docColNameStore = undefined;
      });
    });
  });

  describe('new DocColNameStore page', () => {
    beforeEach(() => {
      cy.visit(`${docColNameStorePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocColNameStore');
    });

    it.skip('should create an instance of DocColNameStore', () => {
      cy.get(`[data-cy="colName"]`).type('high-level Table bi-directional').should('have.value', 'high-level Table bi-directional');

      cy.get(`[data-cy="docStore"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        docColNameStore = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', docColNameStorePageUrlPattern);
    });
  });
});
