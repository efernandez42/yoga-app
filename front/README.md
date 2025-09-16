# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Go inside folder:

> cd yoga

Install dependencies:

> npm install

Launch Front-end:

> npm run start;


## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### Prérequis

- Node.js 16+
- Angular CLI 14+

#### Tests unitaires et d'intégration

Le projet utilise Jest pour les tests unitaires et d'intégration.

**Commandes disponibles :**

- `npm run test` : Exécution unique avec couverture
- `npm run test:watch` : Mode watch pour le développement
- `npm run test:ci` : Mode CI avec exécution séquentielle

**Couverture de code :**

- **Statements : 100%** (objectif : ≥80%) ✅
- **Lines : 100%** (objectif : ≥80%) ✅
- **Functions : 100%** (objectif : ≥80%) ✅
- **Branches : 94.11%** (objectif : ≥80%) ✅

**Rapport de couverture :**

Le rapport HTML est disponible dans :
> front/coverage/lcov-report/index.html

**Types de tests :**

- **Tests unitaires** : Composants, services, pipes isolés avec mocks
- **Tests d'intégration** : Parcours utilisateur complets avec interactions DOM
- **Tests E2E** : Tests end-to-end avec Cypress

**Répartition des tests :**

- **Nombre de tests unitaires : 64**
- **Nombre de tests d'intégration : 31**
- **Proportion d'intégration : 31 / (64+31) = 32.63%** (≥ 30% requis) ✅

### Détail des tests d'intégration :
- `auth-workflow.integration.spec.ts` : 7 tests
- `login.integration.spec.ts` : 3 tests  
- `register.integration.spec.ts` : 3 tests
- `session-error-workflow.integration.spec.ts` : 5 tests
- `teacher.integration.spec.ts` : 6 tests
- `user.integration.spec.ts` : 7 tests



#### Tests E2E avec Cypress

Le projet utilise Cypress pour les tests end-to-end (E2E) qui simulent les parcours utilisateur réels dans le navigateur.

**Commandes disponibles :**

- `npm run e2e:open` : Ouvrir l'interface Cypress pour exécuter les tests interactivement
- `npm run e2e` : Exécuter tous les tests E2E en mode headless avec couverture
- `npm run e2e:ci` : Exécuter les tests E2E avec couverture de code
- `npm run e2e:coverage` : Générer le rapport de couverture
- `powershell -ExecutionPolicy Bypass -File run-e2e-coverage.ps1` : Script complet avec couverture

**Résultats des tests :**

- **Total des tests** : 49 tests E2E
- **Taux de réussite** : 100% (49/49 tests passent)
- **Temps d'exécution** : ~55 secondes
- **Browser** : Chrome 139 (headless)

**Couverture E2E :**

- **Objectif** : ≥ 80% de couverture pour tous les indicateurs (statements, branches, functions, lines)
- **Configuration** : Utilisation de `@cypress/code-coverage` pour la collecte de métriques
- **Rapport** : Génération automatique de rapports de couverture après l'exécution des tests
- **Note** : L'instrumentation de l'application nécessite une configuration spécifique pour atteindre 100% de couverture

**Scénarios testés :**

1. **Authentification** (`auth.cy.ts`) - 5 tests ✅ :
   - Connexion réussie
   - Échec de connexion (401)
   - Validation des formulaires
   - Déconnexion
   - Redirection après connexion

2. **Inscription** (`register.cy.ts`) - 6 tests ✅ :
   - Inscription réussie
   - Échec d'inscription (400)
   - Validation des formulaires
   - Navigation entre pages
   - Gestion des erreurs serveur (500)

3. **Gestion des sessions** (`sessions.cy.ts`) - 5 tests ✅ :
   - Liste des sessions
   - Détail d'une session
   - Gestion des erreurs (404, 500)
   - Listes vides

4. **Administration des sessions** (`sessions-admin.cy.ts`) - 10 tests ✅ :
   - Création de session (201, 400, 500)
   - Suppression de session (200, 403)
   - Modification de session
   - Annulation de suppression
   - Permissions d'administration

5. **Participation aux sessions** (`participation.cy.ts`) - 9 tests ✅ :
   - Participation réussie
   - Conflit de participation (409)
   - Annulation de participation
   - Affichage des informations de participation
   - Maintien de l'état après navigation

6. **Tests simplifiés** - 14 tests ✅ :
   - `login.cy.ts` - 1 test
   - `participation-simple.cy.ts` - 5 tests
   - `sessions-admin-simple.cy.ts` - 4 tests
   - `sessions-simple.cy.ts` - 4 tests

**Mocking des API :**

Tous les appels API sont mockés avec `cy.intercept()` et des fixtures JSON pour garantir la stabilité des tests.

**Rapport de couverture :**

Le rapport HTML est disponible dans :
> front/coverage/lcov-report/index.html
