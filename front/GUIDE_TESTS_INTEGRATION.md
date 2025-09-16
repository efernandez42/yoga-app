# 📊 Guide des Tests d'Intégration

## 🎯 Où voir les résultats des tests d'intégration

### 1. **Exécution des tests d'intégration uniquement**
```bash
npm test -- --testNamePattern="Integration"
```

### 2. **Exécution de tous les tests (unitaires + intégration)**
```bash
npm test
```

### 3. **Mode watch pour le développement**
```bash
npm run test:watch
```

### 4. **Mode CI avec rapport de couverture**
```bash
npm run test:ci
```

## 📈 Résultats des Tests d'Intégration

### Tests d'Intégration Disponibles

#### 🔐 **LoginComponent Integration Tests**
- **Test 1** : `should display login form and handle successful login flow`
  - Vérifie l'affichage du formulaire de connexion
  - Simule la saisie utilisateur (email + mot de passe)
  - Teste la connexion réussie avec navigation vers `/sessions`
  - Vérifie les appels aux services (AuthService, SessionService, Router)

- **Test 2** : `should handle login error and display error message`
  - Simule une erreur de connexion (HTTP 401)
  - Vérifie que l'erreur est affichée correctement
  - S'assure qu'aucune navigation n'a lieu en cas d'erreur

- **Test 3** : `should validate form fields and disable submit when invalid`
  - Teste la validation du formulaire
  - Vérifie que le bouton est désactivé avec des données invalides
  - Teste les validations email et mot de passe

#### 📝 **RegisterComponent Integration Tests**
- **Test 1** : `should display register form and handle successful registration flow`
  - Vérifie l'affichage du formulaire d'inscription
  - Simule la saisie utilisateur (email, prénom, nom, mot de passe)
  - Teste l'inscription réussie avec navigation vers `/login`
  - Vérifie les données envoyées à l'API

- **Test 2** : `should handle registration error and display error message`
  - Simule une erreur d'inscription (HTTP 400)
  - Vérifie que l'erreur est gérée correctement
  - S'assure qu'aucune navigation n'a lieu en cas d'erreur

- **Test 3** : `should validate form fields and disable submit when invalid`
  - Teste la validation du formulaire d'inscription
  - Vérifie les validations pour tous les champs (email, prénom, nom, mot de passe)
  - Teste les contraintes de longueur minimale

## 🔍 Comment lire les résultats

### Dans le terminal :
```
PASS  src/app/features/auth/components/login/login.integration.spec.ts (5.072 s)
PASS  src/app/features/auth/components/register/register.integration.spec.ts (5.207 s)
```

### Dans le rapport de couverture :
- **Statements** : Pourcentage de lignes de code exécutées
- **Branches** : Pourcentage de branches conditionnelles testées
- **Functions** : Pourcentage de fonctions appelées
- **Lines** : Pourcentage de lignes de code couvertes

## 📁 Fichiers des Tests d'Intégration

```
src/app/features/auth/components/
├── login/
│   ├── login.integration.spec.ts    # Tests d'intégration login
│   └── login.component.spec.ts      # Tests unitaires login
└── register/
    ├── register.integration.spec.ts # Tests d'intégration register
    └── register.component.spec.ts   # Tests unitaires register
```

## 🎨 Comment les Tests d'Intégration Fonctionnent

### 1. **Configuration TestBed**
```typescript
await TestBed.configureTestingModule({
  declarations: [LoginComponent],
  imports: [
    RouterTestingModule,      // Pour tester la navigation
    HttpClientTestingModule,  // Pour tester les appels HTTP
    ReactiveFormsModule,      // Pour les formulaires réactifs
    MatSnackBarModule,        // Pour les notifications
    // ... autres modules Angular Material
  ],
  providers: [
    AuthService,              // Service réel
    { provide: SessionService, useValue: sessionServiceSpy }, // Mock
    { provide: Router, useValue: routerSpy } // Mock
  ]
});
```

### 2. **Simulation d'Interactions Utilisateur**
```typescript
// Récupération des éléments DOM
const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

// Simulation de la saisie
emailInput.value = 'test@test.com';
emailInput.dispatchEvent(new Event('input'));
fixture.detectChanges();

// Simulation du clic
submitButton.click();
```

### 3. **Vérification des Appels HTTP**
```typescript
// Vérification de l'appel API
const req = httpTestingController.expectOne('api/auth/login');
expect(req.request.method).toBe('POST');
req.flush(mockSessionInfo); // Simulation de la réponse
```

### 4. **Vérification des Services**
```typescript
// Vérification des appels aux services
expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
```

## 🚀 Avantages des Tests d'Intégration

1. **Test du Comportement Complet** : Vérifient l'interaction entre composants, services et modules
2. **Simulation Réaliste** : Utilisent de vrais modules Angular et des interactions DOM
3. **Détection d'Erreurs** : Trouvent des problèmes que les tests unitaires isolés ne détectent pas
4. **Confiance** : Donnent confiance que l'application fonctionne comme prévu

## 📊 Couverture Actuelle

- **Tests d'Intégration** : 2 composants (Login + Register)
- **Tests Unitaires** : 15 composants et services
- **Couverture Globale** : 100% statements, 94.11% branches, 100% functions, 100% lines
- **Temps d'Exécution** : ~10-12 secondes pour tous les tests

## 🔧 Commandes Utiles

```bash
# Voir seulement les tests d'intégration
npm test -- --testNamePattern="Integration"

# Voir les tests d'un composant spécifique
npm test -- --testNamePattern="LoginComponent"

# Mode verbose pour plus de détails
npm test -- --verbose

# Générer le rapport de couverture
npm run test:ci
```

## 📝 Notes Importantes

- Les tests d'intégration utilisent `HttpClientTestingModule` pour simuler les appels HTTP
- Les modules Angular Material sont importés pour un rendu réaliste
- Les mocks sont utilisés pour les services externes (Router, SessionService)
- Chaque test est indépendant et se nettoie après exécution
