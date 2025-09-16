import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionService } from '../../services/session.service';
import { SessionApiService } from './services/session-api.service';
import { of, throwError } from 'rxjs';

describe('Session Error Workflow - Tests d\'intégration', () => {
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService, SessionApiService]
    });

    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('devrait gérer le workflow de suppression de session avec erreur 403', () => {
    // Configuration d'un utilisateur connecté
    const mockUser = {
      id: 1,
      firstName: 'Test',
      lastName: 'User',
      email: 'test@example.com',
      admin: false,
      token: 'mock-token',
      type: 'Bearer',
      username: 'test@example.com'
    };

    sessionService.logIn(mockUser);
    expect(sessionService.isLogged).toBeTruthy();

    // Test de suppression avec erreur 403
    sessionApiService.delete('1').subscribe({
      next: (result) => {
        // Ceci ne devrait pas être appelé en cas d'erreur 403
        expect(result).toBeUndefined();
      },
      error: (error) => {
        expect(error.status).toBe(403);
        expect(error.error.error).toBe('Forbidden');
      }
    });

    // Simulation de la réponse HTTP 403
    const req = httpMock.expectOne('api/session/1');
    req.flush({ error: 'Forbidden' }, { 
      status: 403, 
      statusText: 'Forbidden' 
    });
  });

  it('devrait gérer le workflow de création de session avec erreur 409', () => {
    // Configuration d'un utilisateur admin connecté
    const mockAdmin = {
      id: 1,
      firstName: 'Admin',
      lastName: 'User',
      email: 'admin@example.com',
      admin: true,
      token: 'mock-token',
      type: 'Bearer',
      username: 'admin@example.com'
    };

    sessionService.logIn(mockAdmin);
    expect(sessionService.isLogged).toBeTruthy();

    // Test de création avec erreur 409
    const newSession = {
      name: 'Session Test',
      date: new Date('2024-01-15'),
      teacher_id: 1,
      description: 'Description test',
      users: []
    };

    sessionApiService.create(newSession).subscribe({
      next: (result) => {
        // Ceci ne devrait pas être appelé en cas d'erreur 409
        expect(result).toBeUndefined();
      },
      error: (error) => {
        expect(error.status).toBe(409);
        expect(error.error.error).toBe('Conflict - Session name already exists');
      }
    });

    // Simulation de la réponse HTTP 409
    const req = httpMock.expectOne('api/session');
    req.flush({ error: 'Conflict - Session name already exists' }, { 
      status: 409, 
      statusText: 'Conflict' 
    });
  });

  it('devrait gérer le workflow de participation avec erreur 500', () => {
    // Configuration d'un utilisateur connecté
    const mockUser = {
      id: 1,
      firstName: 'Test',
      lastName: 'User',
      email: 'test@example.com',
      admin: false,
      token: 'mock-token',
      type: 'Bearer',
      username: 'test@example.com'
    };

    sessionService.logIn(mockUser);
    expect(sessionService.isLogged).toBeTruthy();

    // Test de participation avec erreur 500
    sessionApiService.participate('1', '1').subscribe({
      next: (result) => {
        // Ceci ne devrait pas être appelé en cas d'erreur 500
        expect(result).toBeUndefined();
      },
      error: (error) => {
        expect(error.status).toBe(500);
        expect(error.error.error).toBe('Internal Server Error');
      }
    });

    // Simulation de la réponse HTTP 500
    const req = httpMock.expectOne('api/session/1/participate/1');
    req.flush({ error: 'Internal Server Error' }, { 
      status: 500, 
      statusText: 'Internal Server Error' 
    });
  });

  it('devrait gérer le workflow de récupération des sessions avec erreur 500', () => {
    // Test de récupération des sessions avec erreur 500
    sessionApiService.all().subscribe({
      next: (result) => {
        // Ceci ne devrait pas être appelé en cas d'erreur 500
        expect(result).toBeUndefined();
      },
      error: (error) => {
        expect(error.status).toBe(500);
        expect(error.error.error).toBe('Internal Server Error');
      }
    });

    // Simulation de la réponse HTTP 500
    const req = httpMock.expectOne('api/session');
    req.flush({ error: 'Internal Server Error' }, { 
      status: 500, 
      statusText: 'Internal Server Error' 
    });
  });

  it('devrait gérer le workflow de déconnexion après erreur', () => {
    // Configuration d'un utilisateur connecté
    const mockUser = {
      id: 1,
      firstName: 'Test',
      lastName: 'User',
      email: 'test@example.com',
      admin: false,
      token: 'mock-token',
      type: 'Bearer',
      username: 'test@example.com'
    };

    sessionService.logIn(mockUser);
    expect(sessionService.isLogged).toBeTruthy();

    // Déconnexion
    sessionService.logOut();
    expect(sessionService.isLogged).toBeFalsy();
    expect(sessionService.sessionInformation).toBeUndefined();
  });
});
