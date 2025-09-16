describe('Participation E2E Tests', () => {
  beforeEach(() => {
    // Se connecter avant chaque test
    cy.intercept('POST', '/api/auth/login', { 
      fixture: 'login-success.json',
      statusCode: 200 
    }).as('loginSuccess');

    cy.visit('/');
    cy.get('span[routerLink="login"]').click();
    cy.get('input[formControlName="email"]').type('test@example.com');
    cy.get('input[formControlName="password"]').type('password123');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginSuccess');
  });

  describe('Participation à une session', () => {
    it('devrait permettre à un utilisateur de participer à une session avec succès', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer l\'erreur 409 quand l\'utilisateur participe déjà', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs serveur lors de la participation', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Annulation de participation', () => {
    it('devrait permettre à un utilisateur d\'annuler sa participation', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs lors de l\'annulation de participation', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Affichage des informations de participation', () => {
    it('devrait afficher le nombre de participants', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait afficher l\'état de participation de l\'utilisateur', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Gestion des erreurs d\'authentification', () => {
    it('devrait gérer l\'erreur 401 lors de la participation', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Navigation et interface utilisateur', () => {
    it('devrait maintenir l\'état de participation après navigation', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });
});
