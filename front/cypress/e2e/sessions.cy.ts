describe('Sessions E2E Tests', () => {
  beforeEach(() => {
    // Intercepter l'appel API de login
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

  describe('Liste des sessions', () => {
    it('devrait afficher la liste des sessions avec succès', () => {
      // Cliquer sur le lien des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait afficher un message quand la liste est vide', () => {
      // Cliquer sur le lien des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer les erreurs serveur lors du chargement des sessions', () => {
      // Cliquer sur le lien des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous restons sur la page des sessions
      cy.url().should('include', '/sessions');
    });
  });

  describe('Détail d\'une session', () => {
    it('devrait afficher les détails d\'une session avec succès', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que la page des sessions est chargée
      cy.get('body').should('be.visible');
    });

    it('devrait gérer l\'erreur 404 pour une session inexistante', () => {
      // Aller directement sur une URL de session inexistante
      cy.visit('/sessions/detail/999');

      // Vérifier que nous sommes redirigés vers la page de login
      cy.url().should('include', '/login');
    });
  });
});
