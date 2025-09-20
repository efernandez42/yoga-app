import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/interfaces/user.interface';

import { MeComponent } from './me.component';

// Groupe de tests pour le composant MeComponent
describe('MeComponent', () => {
  let component: MeComponent;// instance du composant à tester
  let fixture: ComponentFixture<MeComponent>; // "boîte" qui contient le composant et son template HTML
  let mockSessionService: jest.Mocked<SessionService>; // service mocké de session
  let mockUserService: jest.Mocked<UserService>; // service mocké d'utilisateur
  let mockRouter: jest.Mocked<Router>; // mock du router Angular
  let mockSnackBar: jest.Mocked<MatSnackBar>; // mock pour afficher les notifications

  // Données fictives d’un utilisateur simulé
  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  // Avant chaque test → configuration de l’environnement de test
  beforeEach(async () => {
    const sessionServiceSpy = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      logOut: jest.fn() // simule la méthode logOut
    };

    // Faux service utilisateur
    const userServiceSpy = {
      getById: jest.fn(), // simule getById()
      delete: jest.fn() // simule delete()
    };

  // Faux router
    const routerSpy = {
      navigate: jest.fn() // simule la navigation
    };

    // Faux snackBar
    const snackBarSpy = {
      open: jest.fn() // simule l’ouverture d’un message
    };

    // Configuration de TestBed (environnement Angular de test)
    await TestBed.configureTestingModule({
      declarations: [MeComponent], // composant à tester
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MatSnackBar, useValue: snackBarSpy }
      ],
    })
      .compileComponents();

    // Création d’une instance du composant
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    // Injection des services mockés
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockUserService = TestBed.inject(UserService) as jest.Mocked<UserService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    mockSnackBar = TestBed.inject(MatSnackBar) as jest.Mocked<MatSnackBar>;
  });

  // Test 1 : Vérifie que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test 2 : Vérifie que les données utilisateur sont chargées au démarrage (ngOnInit)
  it('should load user data on init', () => {
    // Simule le retour d’un utilisateur quand getById() est appelé
    mockUserService.getById.mockReturnValue(of(mockUser));

    // Exécute ngOnInit (méthode appelée à l’initialisation du composant)
    component.ngOnInit();

    // Vérifie que getById a bien été appelé avec l’ID "1"
    expect(mockUserService.getById).toHaveBeenCalledWith('1');

    // Vérifie que l’utilisateur dans le composant correspond au mock
    expect(component.user).toEqual(mockUser);
  });

  // Test 3 : Vérifie que la méthode back() appelle bien l’historique du navigateur
  it('should call window.history.back when back is called', () => {
    // Espionne la méthode window.history.back
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});

    // Appelle la méthode back() du composant
    component.back();

    // Vérifie que window.history.back a bien été appelée
    expect(historySpy).toHaveBeenCalled();

    // Restaure le comportement original
    historySpy.mockRestore();
  });

  // Test 4 : Vérifie que delete() supprime un utilisateur et redirige vers la home
  it('should delete user and navigate to home when delete is called', () => {
    // Simule la suppression réussie de l’utilisateur
    mockUserService.delete.mockReturnValue(of({}));

    // Appelle la méthode delete() du composant
    component.delete();

    // Vérifie que le service a bien été appelé
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    // Vérifie qu’un message SnackBar s’affiche
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    // Vérifie que l’utilisateur est déconnecté
    expect(mockSessionService.logOut).toHaveBeenCalled();
    // Vérifie que l’utilisateur est redirigé vers la page d’accueil
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
