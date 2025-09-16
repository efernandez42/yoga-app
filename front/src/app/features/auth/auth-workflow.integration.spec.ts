// Imports nécessaires pour les tests d'intégration de parcours complets
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthService } from './services/auth.service';
import { SessionService } from '../../services/session.service';
import { SessionInformation } from '../../interfaces/sessionInformation.interface';

/**
 * Tests d'intégration pour les parcours complets d'authentification
 * Ces tests vérifient les flux utilisateur complets avec navigation
 */
describe('Auth Workflow Integration Tests', () => {
  let httpTestingController: HttpTestingController;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: jest.Mocked<Router>;

  // Données de test
  const mockSessionInfo: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'admin@test.com',
    firstName: 'Admin',
    lastName: 'User',
    admin: true
  };

  // Configuration avant chaque test
  beforeEach(async () => {
    // Mock du service de session
    const sessionServiceSpy = {
      logIn: jest.fn(),
      logOut: jest.fn(),
      sessionInformation: undefined
    };

    // Mock du router
    const routerSpy = {
      navigate: jest.fn()
    };

    // Configuration du module de test
    await TestBed.configureTestingModule({
      declarations: [LoginComponent, RegisterComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: LoginComponent },
          { path: 'register', component: RegisterComponent },
          { path: 'sessions', redirectTo: '' }
        ]),
        HttpClientTestingModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        BrowserAnimationsModule
      ],
      providers: [
        AuthService,
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
  });

  // Nettoyage après chaque test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Test d'intégration : Parcours complet de connexion admin
  it('should complete admin login workflow successfully', async () => {
    // Création du composant de connexion
    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    // Remplissage du formulaire de connexion
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    emailInput.value = 'admin@test.com';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'admin123';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Vérification de l'appel HTTP de connexion
    const loginReq = httpTestingController.expectOne('api/auth/login');
    expect(loginReq.request.method).toBe('POST');
    loginReq.flush(mockSessionInfo);

    // Vérification des appels aux services
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Test d'intégration : Parcours complet d'inscription utilisateur
  it('should complete user registration workflow successfully', async () => {
    // Création du composant d'inscription
    const fixture = TestBed.createComponent(RegisterComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    // Remplissage du formulaire d'inscription
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const firstNameInput = fixture.debugElement.query(By.css('input[formControlName="firstName"]')).nativeElement;
    const lastNameInput = fixture.debugElement.query(By.css('input[formControlName="lastName"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    emailInput.value = 'newuser@test.com';
    emailInput.dispatchEvent(new Event('input'));
    firstNameInput.value = 'New';
    firstNameInput.dispatchEvent(new Event('input'));
    lastNameInput.value = 'User';
    lastNameInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'newpassword123';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Vérification de l'appel HTTP d'inscription
    const registerReq = httpTestingController.expectOne('api/auth/register');
    expect(registerReq.request.method).toBe('POST');
    registerReq.flush({});

    // Vérification de la navigation vers la page de connexion
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  // Test d'intégration : Gestion des erreurs de connexion
  it('should handle login error and display appropriate message', async () => {
    // Création du composant de connexion
    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    // Remplissage du formulaire avec des identifiants incorrects
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    emailInput.value = 'wrong@test.com';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'wrongpassword';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Simulation d'une erreur de connexion
    const loginReq = httpTestingController.expectOne('api/auth/login');
    expect(loginReq.request.method).toBe('POST');
    loginReq.flush({}, { status: 401, statusText: 'Unauthorized' });

    // Vérification que l'erreur est gérée
    expect(component.onError).toBe(true);
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  // Test d'intégration : Gestion des erreurs d'inscription
  it('should handle registration error and display appropriate message', async () => {
    // Création du composant d'inscription
    const fixture = TestBed.createComponent(RegisterComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    // Remplissage du formulaire avec un email déjà existant
    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const firstNameInput = fixture.debugElement.query(By.css('input[formControlName="firstName"]')).nativeElement;
    const lastNameInput = fixture.debugElement.query(By.css('input[formControlName="lastName"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    emailInput.value = 'existing@test.com';
    emailInput.dispatchEvent(new Event('input'));
    firstNameInput.value = 'Existing';
    firstNameInput.dispatchEvent(new Event('input'));
    lastNameInput.value = 'User';
    lastNameInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'password123';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Simulation d'une erreur d'inscription
    const registerReq = httpTestingController.expectOne('api/auth/register');
    expect(registerReq.request.method).toBe('POST');
    registerReq.flush({}, { status: 400, statusText: 'Bad Request' });

    // Vérification que l'erreur est gérée
    expect(component.onError).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  // Test d'intégration : Validation des formulaires
  it('should validate forms and disable submit when invalid', async () => {
    // Test du formulaire de connexion
    const loginFixture = TestBed.createComponent(LoginComponent);
    const loginComponent = loginFixture.componentInstance;
    loginFixture.detectChanges();

    const emailInput = loginFixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput = loginFixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = loginFixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    // Formulaire vide
    expect(submitButton.disabled).toBe(true);

    // Email invalide
    emailInput.value = 'invalid-email';
    emailInput.dispatchEvent(new Event('input'));
    loginFixture.detectChanges();
    expect(submitButton.disabled).toBe(true);

    // Test du formulaire d'inscription
    const registerFixture = TestBed.createComponent(RegisterComponent);
    const registerComponent = registerFixture.componentInstance;
    registerFixture.detectChanges();

    const registerSubmitButton = registerFixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;
    expect(registerSubmitButton.disabled).toBe(true);
  });

  // Test d'intégration : Parcours de connexion avec des données complexes
  it('should handle login with complex email addresses', async () => {
    const fixture = TestBed.createComponent(LoginComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    // Test avec un email complexe
    emailInput.value = 'user.name+tag@example-domain.co.uk';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'complexpassword123';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Vérification que le formulaire est valide
    expect(submitButton.disabled).toBe(false);

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Vérification de l'appel HTTP
    const loginReq = httpTestingController.expectOne('api/auth/login');
    expect(loginReq.request.method).toBe('POST');
    loginReq.flush(mockSessionInfo);
  });

  // Test d'intégration : Parcours d'inscription avec des noms complexes
  it('should handle registration with complex names', async () => {
    const fixture = TestBed.createComponent(RegisterComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    const emailInput = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const firstNameInput = fixture.debugElement.query(By.css('input[formControlName="firstName"]')).nativeElement;
    const lastNameInput = fixture.debugElement.query(By.css('input[formControlName="lastName"]')).nativeElement;
    const passwordInput = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    // Test avec des noms complexes
    emailInput.value = 'jean-pierre@test.com';
    emailInput.dispatchEvent(new Event('input'));
    firstNameInput.value = 'Jean-Pierre';
    firstNameInput.dispatchEvent(new Event('input'));
    lastNameInput.value = "O'Connor-Smith";
    lastNameInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'complexpassword123';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Vérification que le formulaire est valide
    expect(submitButton.disabled).toBe(false);

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Vérification de l'appel HTTP
    const registerReq = httpTestingController.expectOne('api/auth/register');
    expect(registerReq.request.method).toBe('POST');
    registerReq.flush({});
  });
});
