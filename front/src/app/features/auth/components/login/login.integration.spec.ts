// Imports nécessaires pour les tests d'intégration
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { By } from '@angular/platform-browser';

/**
 * Tests d'intégration pour le composant de connexion
 * Ces tests vérifient le comportement complet du composant avec les vrais modules Angular
 */
describe('LoginComponent Integration Tests', () => {
  // Variables pour les tests
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpTestingController: HttpTestingController;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: jest.Mocked<Router>;

  // Données de test pour simuler une session utilisateur
  const mockSessionInfo: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  // Configuration avant chaque test
  beforeEach(async () => {
    // Mock du service de session pour simuler la connexion
    const sessionServiceSpy = {
      logIn: jest.fn()
    };

    // Mock du router pour simuler la navigation
    const routerSpy = {
      navigate: jest.fn()
    };

    // Configuration du module de test avec tous les modules nécessaires
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule.withRoutes([{ path: 'sessions', redirectTo: '' }]), // Module de test pour le routing
        HttpClientTestingModule, // Module de test pour les appels HTTP
        MatSnackBarModule, // Module Angular Material pour les notifications
        ReactiveFormsModule, // Module pour les formulaires réactifs
        BrowserAnimationsModule, // Module pour les animations
        MatInputModule, // Module Angular Material pour les champs de saisie
        MatFormFieldModule, // Module Angular Material pour les champs de formulaire
        MatCardModule, // Module Angular Material pour les cartes
        MatButtonModule, // Module Angular Material pour les boutons
        MatIconModule // Module Angular Material pour les icônes
      ],
      providers: [
        AuthService, // Service d'authentification réel
        { provide: SessionService, useValue: sessionServiceSpy }, // Mock du service de session
        { provide: Router, useValue: routerSpy } // Mock du router
      ]
    }).compileComponents();

    // Création du composant et récupération des services
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpTestingController = TestBed.inject(HttpTestingController);
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    fixture.detectChanges(); // Déclenche la détection des changements
  });

  // Nettoyage après chaque test
  afterEach(() => {
    httpTestingController.verify(); // Vérifie qu'il n'y a pas d'appels HTTP en attente
  });

  // Test d'intégration : Affichage du formulaire et connexion réussie
  it('should display login form and handle successful login flow', async () => {
    // Vérification de l'affichage des éléments du formulaire
    const emailInput: HTMLInputElement = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput: HTMLInputElement = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton: HTMLButtonElement = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    // Vérification que tous les éléments sont présents
    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();
    expect(submitButton).toBeTruthy();

    // Simulation de la saisie utilisateur
    emailInput.value = 'test@test.com';
    emailInput.dispatchEvent(new Event('input')); // Déclenche l'événement de saisie
    passwordInput.value = 'password123';
    passwordInput.dispatchEvent(new Event('input')); // Déclenche l'événement de saisie
    fixture.detectChanges(); // Met à jour la vue

    // Vérification que le bouton est activé après saisie valide
    expect(submitButton.disabled).toBe(false);

    // Simulation de la soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Vérification de l'appel HTTP à l'API de connexion
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockSessionInfo); // Simule une réponse réussie

    // Vérification des appels aux services
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false); // Pas d'erreur
  });

  // Test d'intégration : Gestion des erreurs de connexion
  it('should handle login error and display error message', async () => {
    // Récupération des éléments du formulaire
    const emailInput: HTMLInputElement = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput: HTMLInputElement = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton: HTMLButtonElement = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    // Simulation de la saisie avec des identifiants incorrects
    emailInput.value = 'test@test.com';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'wrongpassword';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Soumission du formulaire
    submitButton.click();
    fixture.detectChanges();

    // Simulation d'une erreur HTTP 401 (Non autorisé)
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({}, { status: 401, statusText: 'Unauthorized' });

    // Vérification que l'erreur est gérée correctement
    expect(component.onError).toBe(true);
    expect(mockSessionService.logIn).not.toHaveBeenCalled(); // Le service de session ne doit pas être appelé
    expect(mockRouter.navigate).not.toHaveBeenCalled(); // Pas de navigation en cas d'erreur
  });

  // Test d'intégration : Validation du formulaire
  it('should validate form fields and disable submit when invalid', async () => {
    // Récupération des éléments du formulaire
    const emailInput: HTMLInputElement = fixture.debugElement.query(By.css('input[formControlName="email"]')).nativeElement;
    const passwordInput: HTMLInputElement = fixture.debugElement.query(By.css('input[formControlName="password"]')).nativeElement;
    const submitButton: HTMLButtonElement = fixture.debugElement.query(By.css('button[type="submit"]')).nativeElement;

    // Test avec un formulaire vide
    expect(submitButton.disabled).toBe(true);

    // Test avec un email invalide
    emailInput.value = 'invalid-email';
    emailInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(submitButton.disabled).toBe(true);

    // Test avec un mot de passe trop court
    emailInput.value = 'test@test.com';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = '12';
    passwordInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(submitButton.disabled).toBe(true);
  });
});