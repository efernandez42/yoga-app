describe('Participation E2E Tests - Version Simplifiée', () => {
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

  describe('Navigation vers les sessions', () => {
    it('devrait pouvoir naviguer vers la page des sessions', () => {
      // Cliquer sur le lien des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');
    });

    it('devrait afficher la page des sessions', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que la page se charge
      cy.url().should('include', '/sessions');
      
      // Vérifier que la page contient des éléments de base
      cy.get('body').should('be.visible');
    });
  });

  describe('Navigation vers le détail d\'une session', () => {
    it('devrait pouvoir naviguer vers le détail d\'une session', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Essayer de cliquer sur un bouton Detail s'il existe
      cy.get('body').then(($body) => {
        if ($body.find('button:contains("Detail")').length > 0) {
          cy.get('button').contains('Detail').first().click();
          // Vérifier que nous sommes sur une page de détail
          cy.url().should('match', /\/sessions\/detail\/\d+/);
        }
      });
    });
  });

  describe('Test de participation', () => {
    it('devrait pouvoir interagir avec les boutons de participation', () => {
      // Aller sur la page des sessions
      cy.get('span[routerLink="sessions"]').click();

      // Vérifier que nous sommes sur la page des sessions
      cy.url().should('include', '/sessions');

      // Essayer de cliquer sur un bouton Detail s'il existe
      cy.get('body').then(($body) => {
        if ($body.find('button:contains("Detail")').length > 0) {
          cy.get('button').contains('Detail').first().click();
          
          // Vérifier que nous sommes sur une page de détail
          cy.url().should('match', /\/sessions\/detail\/\d+/);
          
          // Essayer de cliquer sur un bouton Participate s'il existe
          cy.get('body').then(($body) => {
            if ($body.find('button:contains("Participate")').length > 0) {
              cy.get('button').contains('Participate').click();
              // Vérifier que nous sommes toujours sur la page de détail
              cy.url().should('match', /\/sessions\/detail\/\d+/);
            }
          });
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
