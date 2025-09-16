// Imports nécessaires pour les tests unitaires
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

import { SessionService } from './session.service';

/**
 * Tests unitaires pour le service de session
 * Ces tests vérifient la gestion de l'état de connexion de l'utilisateur
 */
describe('SessionService', () => {
  let service: SessionService;

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
  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  // Test de création du service
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test de l'état initial (déconnecté)
  it('should initialize with logged out state', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  // Test de l'observable d'état de connexion
  it('should return observable for login state', (done) => {
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false); // État initial : déconnecté
      done();
    });
  });

  // Test de connexion utilisateur
  it('should log in user and update state', () => {
    service.logIn(mockSessionInfo);

    // Vérification que l'utilisateur est connecté
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInfo);
  });

  // Test d'émission d'événement lors de la connexion
  it('should emit login state change when logging in', (done) => {
    service.$isLogged().subscribe(isLogged => {
      if (isLogged) {
        expect(isLogged).toBe(true);
        done();
      }
    });

    service.logIn(mockSessionInfo);
  });

  // Test de déconnexion utilisateur
  it('should log out user and update state', () => {
    // Connexion d'abord
    service.logIn(mockSessionInfo);
    // Puis déconnexion
    service.logOut();

    // Vérification que l'utilisateur est déconnecté
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  // Test d'émission d'événement lors de la déconnexion
  it('should emit login state change when logging out', (done) => {
    // Connexion d'abord
    service.logIn(mockSessionInfo);
    
    let callCount = 0;
    service.$isLogged().subscribe(isLogged => {
      callCount++;
      if (callCount === 2) { // Deuxième appel = déconnexion
        expect(isLogged).toBe(false);
        done();
      }
    });

    service.logOut();
  });
});
