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

describe('DocStoreAccessAudit e2e test', () => {
  const docStoreAccessAuditPageUrl = '/doc-store-access-audit';
  const docStoreAccessAuditPageUrlPattern = new RegExp('/doc-store-access-audit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const docStoreAccessAuditSample = {};

  let docStoreAccessAudit;
  // let user;
  // let docStore;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"id":"4d2f60cc-c88b-4e1c-91e5-cca1881d72d8","login":"Christmas Stand-alone Assistant","firstName":"Willard","lastName":"Bayer"},
    }).then(({ body }) => {
      user = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/doc-stores',
      body: {"fileName":"deposit","fileObject":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileObjectContentType":"unknown","process_status":0},
    }).then(({ body }) => {
      docStore = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/doc-store-access-audits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/doc-store-access-audits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/doc-store-access-audits/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/doc-stores', {
      statusCode: 200,
      body: [docStore],
    });

  });
   */

  afterEach(() => {
    if (docStoreAccessAudit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/doc-store-access-audits/${docStoreAccessAudit.id}`,
      }).then(() => {
        docStoreAccessAudit = undefined;
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

  it('DocStoreAccessAudits menu should load DocStoreAccessAudits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('doc-store-access-audit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocStoreAccessAudit').should('exist');
    cy.url().should('match', docStoreAccessAuditPageUrlPattern);
  });

  describe('DocStoreAccessAudit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(docStoreAccessAuditPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocStoreAccessAudit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/doc-store-access-audit/new$'));
        cy.getEntityCreateUpdateHeading('DocStoreAccessAudit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStoreAccessAuditPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/doc-store-access-audits',
          body: {
            ...docStoreAccessAuditSample,
            user: user,
            docStore: docStore,
          },
        }).then(({ body }) => {
          docStoreAccessAudit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/doc-store-access-audits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/doc-store-access-audits?page=0&size=20>; rel="last",<http://localhost/api/doc-store-access-audits?page=0&size=20>; rel="first"',
              },
              body: [docStoreAccessAudit],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(docStoreAccessAuditPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(docStoreAccessAuditPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DocStoreAccessAudit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('docStoreAccessAudit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStoreAccessAuditPageUrlPattern);
      });

      it('edit button click should load edit DocStoreAccessAudit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocStoreAccessAudit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStoreAccessAuditPageUrlPattern);
      });

      it('edit button click should load edit DocStoreAccessAudit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocStoreAccessAudit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStoreAccessAuditPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DocStoreAccessAudit', () => {
        cy.intercept('GET', '/api/doc-store-access-audits/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('docStoreAccessAudit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', docStoreAccessAuditPageUrlPattern);

        docStoreAccessAudit = undefined;
      });
    });
  });

  describe('new DocStoreAccessAudit page', () => {
    beforeEach(() => {
      cy.visit(`${docStoreAccessAuditPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocStoreAccessAudit');
    });

    it.skip('should create an instance of DocStoreAccessAudit', () => {
      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="docStore"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        docStoreAccessAudit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', docStoreAccessAuditPageUrlPattern);
    });
  });
});
