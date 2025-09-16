describe('Authentication E2E Tests', () => {
  beforeEach(() => {
    // Visiter la page d'accueil avant chaque test
    cy.visit('/');
  });

  describe('Connexion réussie', () => {
    it('devrait permettre la connexion avec des identifiants valides', () => {
      // Intercepter l'appel API de connexion avec succès
      cy.intercept('POST', '/api/auth/login', { 
        fixture: 'login-success.json',
        statusCode: 200 
      }).as('loginSuccess');

      // Cliquer sur le bouton de connexion
      cy.get('span[routerLink="login"]').click();

      // Vérifier que nous sommes sur la page de connexion
      cy.url().should('include', '/login');

      // Remplir le formulaire de connexion
      cy.get('input[formControlName="email"]').type('test@example.com');
      cy.get('input[formControlName="password"]').type('password123');

      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();

      // Attendre la réponse de l'API
      cy.wait('@loginSuccess');

      // Vérifier que nous sommes redirigés vers la page des sessions
      cy.url().should('include', '/sessions');

      // Vérifier que l'utilisateur est connecté (bouton de déconnexion visible)
      cy.get('span[routerLink="me"]').should('be.visible');
      cy.get('span').contains('Logout').should('be.visible');
    });
  });

  describe('Connexion échouée', () => {
    it('devrait afficher une erreur avec des identifiants invalides', () => {
      // Intercepter l'appel API de connexion avec erreur
      cy.intercept('POST', '/api/auth/login', { 
        fixture: 'login-error.json',
        statusCode: 401 
      }).as('loginError');

      // Cliquer sur le bouton de connexion
      cy.get('span[routerLink="login"]').click();

      // Remplir le formulaire avec des identifiants invalides
      cy.get('input[formControlName="email"]').type('wrong@example.com');
      cy.get('input[formControlName="password"]').type('wrongpassword');

      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();

      // Attendre la réponse de l'API
      cy.wait('@loginError');

      // Vérifier que nous restons sur la page de connexion
      cy.url().should('include', '/login');

      // Vérifier qu'un message d'erreur est affiché dans le composant
      cy.get('.error').should('be.visible');
      cy.get('.error').should('contain', 'An error occurred');
    });

    it('devrait gérer les erreurs de validation du formulaire', () => {
      // Cliquer sur le bouton de connexion
      cy.get('span[routerLink="login"]').click();

      // Vérifier que le bouton est désactivé quand le formulaire est vide
      cy.get('button[type="submit"]').should('be.disabled');

      // Remplir seulement l'email
      cy.get('input[formControlName="email"]').type('test@example.com');
      cy.get('button[type="submit"]').should('be.disabled');

      // Remplir le mot de passe avec moins de 3 caractères
      cy.get('input[formControlName="password"]').type('ab');
      cy.get('button[type="submit"]').should('be.disabled');

      // Vérifier que le formulaire est invalide
      cy.get('form').should('have.class', 'ng-invalid');
    });
  });

  describe('Déconnexion', () => {
    it('devrait permettre la déconnexion après connexion', () => {
      // Se connecter d'abord
      cy.intercept('POST', '/api/auth/login', { 
        fixture: 'login-success.json',
        statusCode: 200 
      }).as('loginSuccess');

      cy.get('span[routerLink="login"]').click();
      cy.get('input[formControlName="email"]').type('test@example.com');
      cy.get('input[formControlName="password"]').type('password123');
      cy.get('button[type="submit"]').click();
      cy.wait('@loginSuccess');

      // Vérifier que l'utilisateur est connecté
      cy.get('span').contains('Logout').should('be.visible');

      // Cliquer sur le bouton de déconnexion
      cy.get('span').contains('Logout').click();

      // Vérifier que l'utilisateur est déconnecté
      cy.get('span[routerLink="login"]').should('be.visible');
      cy.get('span').contains('Logout').should('not.exist');
    });
  });

  describe('Navigation après connexion', () => {
    it('devrait rediriger vers la page de profil après connexion', () => {
      // Intercepter l'appel API de connexion
      cy.intercept('POST', '/api/auth/login', { 
        fixture: 'login-success.json',
        statusCode: 200 
      }).as('loginSuccess');

      // Se connecter
      cy.get('span[routerLink="login"]').click();
      cy.get('input[formControlName="email"]').type('test@example.com');
      cy.get('input[formControlName="password"]').type('password123');
      cy.get('button[type="submit"]').click();
      cy.wait('@loginSuccess');

      // Cliquer sur le lien "Me"
      cy.get('span[routerLink="me"]').click();

      // Vérifier que nous sommes sur la page de profil
      cy.url().should('include', '/me');
      // Note: Le contenu exact de la page Me dépend de l'implémentation
    });
  });
});
