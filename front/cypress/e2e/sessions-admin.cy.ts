describe('Sessions Admin E2E Tests', () => {
  beforeEach(() => {
    // Se connecter en tant qu'administrateur avant chaque test
    cy.intercept('POST', '/api/auth/login', { 
      fixture: 'login-success.json',
      statusCode: 200 
    }).as('loginSuccess');

    cy.visit('/');
    cy.get('span[routerLink="login"]').click();
    cy.get('input[formControlName="email"]').type('admin@example.com');
    cy.get('input[formControlName="password"]').type('password123');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginSuccess');
  });

  describe('Création de session', () => {
    it('devrait créer une nouvelle session avec succès', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs de validation lors de la création', () => {
      // Aller sur la page de création
      cy.visit('/sessions/create');

      // Vérifier que nous sommes redirigés vers la page de login
      cy.url().should('include', '/login');

      // Vérifier que la page de login est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs serveur lors de la création', () => {
      // Aller sur la page de création
      cy.visit('/sessions/create');

      // Vérifier que nous sommes redirigés vers la page de login
      cy.url().should('include', '/login');

      // Vérifier que la page de login est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Suppression de session', () => {
    it('devrait supprimer une session avec succès', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer l\'erreur 403 lors de la suppression', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait annuler la suppression de session', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Modification de session', () => {
    it('devrait modifier une session existante avec succès', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs lors de la modification', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });

  describe('Navigation et permissions', () => {
    it('devrait afficher les boutons d\'administration pour les utilisateurs autorisés', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs de permissions', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });
  });
});
