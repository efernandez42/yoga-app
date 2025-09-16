describe('Registration E2E Tests', () => {
  beforeEach(() => {
    // Visiter la page d'accueil avant chaque test
    cy.visit('/');
  });

  describe('Inscription réussie', () => {
    it('devrait permettre l\'inscription avec des données valides', () => {
      // Intercepter l'appel API d'inscription avec succès
      cy.intercept('POST', '/api/auth/register', { 
        fixture: 'register-success.json',
        statusCode: 201 
      }).as('registerSuccess');

      // Cliquer sur le bouton d'inscription
      cy.get('span[routerLink="register"]').click();

      // Vérifier que nous sommes sur la page d'inscription
      cy.url().should('include', '/register');

      // Remplir le formulaire d'inscription
      cy.get('input[formControlName="firstName"]').type('New');
      cy.get('input[formControlName="lastName"]').type('User');
      cy.get('input[formControlName="email"]').type('newuser@example.com');
      cy.get('input[formControlName="password"]').type('password123');

      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();

      // Attendre la réponse de l'API
      cy.wait('@registerSuccess');

      // Vérifier que nous sommes redirigés vers la page de connexion
      cy.url().should('include', '/login');

      // Vérifier que l'utilisateur n'est pas encore connecté
      cy.get('span[routerLink="login"]').should('be.visible');
      cy.get('span[routerLink="register"]').should('be.visible');
    });
  });

  describe('Inscription échouée', () => {
    it('devrait afficher une erreur si l\'email existe déjà', () => {
      // Intercepter l'appel API d'inscription avec erreur
      cy.intercept('POST', '/api/auth/register', { 
        fixture: 'register-error.json',
        statusCode: 400 
      }).as('registerError');

      // Cliquer sur le bouton d'inscription
      cy.get('span[routerLink="register"]').click();

      // Remplir le formulaire avec un email existant
      cy.get('input[formControlName="firstName"]').type('Existing');
      cy.get('input[formControlName="lastName"]').type('User');
      cy.get('input[formControlName="email"]').type('existing@example.com');
      cy.get('input[formControlName="password"]').type('password123');

      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();

      // Attendre la réponse de l'API
      cy.wait('@registerError');

      // Vérifier que nous restons sur la page d'inscription
      cy.url().should('include', '/register');

      // Vérifier qu'un message d'erreur est affiché dans le composant
      cy.get('.error').should('be.visible');
      cy.get('.error').should('contain', 'An error occurred');
    });

    it('devrait gérer les erreurs de validation du formulaire', () => {
      // Cliquer sur le bouton d'inscription
      cy.get('span[routerLink="register"]').click();

      // Vérifier que le bouton est désactivé quand le formulaire est vide
      cy.get('button[type="submit"]').should('be.disabled');

      // Remplir seulement le prénom
      cy.get('input[formControlName="firstName"]').type('Test');
      cy.get('button[type="submit"]').should('be.disabled');

      // Remplir le nom avec moins de 3 caractères
      cy.get('input[formControlName="lastName"]').type('Us');
      cy.get('button[type="submit"]').should('be.disabled');

      // Remplir un email invalide
      cy.get('input[formControlName="email"]').type('invalid-email');
      cy.get('button[type="submit"]').should('be.disabled');

      // Vérifier que le formulaire est invalide
      cy.get('form').should('have.class', 'ng-invalid');
    });

    it('devrait valider la longueur minimale du mot de passe', () => {
      // Cliquer sur le bouton d'inscription
      cy.get('span[routerLink="register"]').click();

      // Remplir tous les champs sauf le mot de passe
      cy.get('input[formControlName="firstName"]').type('Test');
      cy.get('input[formControlName="lastName"]').type('User');
      cy.get('input[formControlName="email"]').type('test@example.com');

      // Remplir un mot de passe trop court
      cy.get('input[formControlName="password"]').type('ab');
      cy.get('button[type="submit"]').should('be.disabled');

      // Vérifier le message d'erreur
      // Vérifier que le formulaire est invalide
      cy.get('form').should('have.class', 'ng-invalid');
    });
  });

  describe('Navigation entre login et register', () => {
    it('devrait permettre de naviguer entre les pages de connexion et d\'inscription', () => {
      // Aller sur la page de connexion
      cy.get('span[routerLink="login"]').click();
      cy.url().should('include', '/login');

      // Cliquer sur le lien d'inscription
      cy.get('span[routerLink="register"]').click();
      cy.url().should('include', '/register');

      // Cliquer sur le lien de connexion
      cy.get('span[routerLink="login"]').click();
      cy.url().should('include', '/login');
    });
  });

  describe('Gestion des erreurs serveur', () => {
    it('devrait gérer les erreurs 500 du serveur', () => {
      // Intercepter l'appel API avec erreur serveur
      cy.intercept('POST', '/api/auth/register', { 
        fixture: 'server-error.json',
        statusCode: 500 
      }).as('serverError');

      // Cliquer sur le bouton d'inscription
      cy.get('span[routerLink="register"]').click();

      // Remplir le formulaire
      cy.get('input[formControlName="firstName"]').type('Test');
      cy.get('input[formControlName="lastName"]').type('User');
      cy.get('input[formControlName="email"]').type('test@example.com');
      cy.get('input[formControlName="password"]').type('password123');

      // Soumettre le formulaire
      cy.get('button[type="submit"]').click();

      // Attendre la réponse de l'API
      cy.wait('@serverError');

      // Vérifier qu'un message d'erreur serveur est affiché
      // Vérifier qu'un message d'erreur est affiché dans le composant
      cy.get('.error').should('be.visible');
      cy.get('.error').should('contain', 'An error occurred');
    });
  });
});
