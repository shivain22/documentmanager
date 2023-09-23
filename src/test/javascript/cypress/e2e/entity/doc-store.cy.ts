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

describe('DocStore e2e test', () => {
  const docStorePageUrl = '/doc-store';
  const docStorePageUrlPattern = new RegExp('/doc-store(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const docStoreSample = {"fileName":"back-end","fileObject":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileObjectContentType":"unknown"};

  let docStore;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"id":"50719364-8915-437b-b8dc-873559fa05ba","login":"withdrawal Loan Berkshire","firstName":"Linnie","lastName":"Frami"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/doc-stores+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/doc-stores').as('postEntityRequest');
    cy.intercept('DELETE', '/api/doc-stores/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

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

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('DocStores menu should load DocStores page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('doc-store');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocStore').should('exist');
    cy.url().should('match', docStorePageUrlPattern);
  });

  describe('DocStore page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(docStorePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocStore page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/doc-store/new$'));
        cy.getEntityCreateUpdateHeading('DocStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStorePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/doc-stores',
          body: {
            ...docStoreSample,
            user: user,
          },
        }).then(({ body }) => {
          docStore = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/doc-stores+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/doc-stores?page=0&size=20>; rel="last",<http://localhost/api/doc-stores?page=0&size=20>; rel="first"',
              },
              body: [docStore],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(docStorePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(docStorePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DocStore page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('docStore');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStorePageUrlPattern);
      });

      it('edit button click should load edit DocStore page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocStore');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStorePageUrlPattern);
      });

      it('edit button click should load edit DocStore page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocStore');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStorePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DocStore', () => {
        cy.intercept('GET', '/api/doc-stores/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('docStore').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStorePageUrlPattern);

        docStore = undefined;
      });
    });
  });

  describe('new DocStore page', () => {
    beforeEach(() => {
      cy.visit(`${docStorePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocStore');
    });

    it.skip('should create an instance of DocStore', () => {
      cy.get(`[data-cy="fileName"]`).type('state').should('have.value', 'state');

      cy.setFieldImageAsBytesOfEntity('fileObject', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="user"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        docStore = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', docStorePageUrlPattern);
    });
  });
});
