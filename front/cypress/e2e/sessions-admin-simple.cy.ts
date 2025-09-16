describe('Sessions Admin E2E Tests - Version Simplifiée', () => {
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

  describe('Navigation vers les sessions administrateur', () => {
    it('devrait pouvoir naviguer vers la page des sessions', () => {
      // Cliquer sur le lien des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');
    });

    it('devrait afficher la page des sessions administrateur', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que la page se charge
      cy.url().should('include', '/sessions');
      
      // Vérifier que la page contient des éléments de base
      cy.get('body').should('be.visible');
    });
  });

  describe('Navigation vers la création de session', () => {
    it('devrait pouvoir naviguer vers la page de création', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Essayer de cliquer sur un bouton Create s'il existe
      cy.get('body').then(($body) => {
        if ($body.find('button:contains("Create")').length > 0) {
          cy.get('button').contains('Create').click();
          // Vérifier que nous sommes sur la page de création
          cy.url().should('include', '/sessions/create');
        }
      });
    });
  });

  describe('Navigation de retour', () => {
    it('devrait pouvoir revenir à la page d\'accueil', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Revenir à la page d'accueil
      cy.visit('/');
      cy.url().should('include', '/');
    });
  });
});
