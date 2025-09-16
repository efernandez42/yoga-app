// Imports nécessaires pour les tests d'intégration
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

/**
 * Tests d'intégration pour le service UserService
 * Ces tests vérifient les appels HTTP réels avec HttpClientTestingModule
 */
describe('UserService Integration Tests', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  // Données de test
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

  // Configuration avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Module de test pour les appels HTTP
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  // Nettoyage après chaque test
  afterEach(() => {
    httpTestingController.verify(); // Vérifie qu'il n'y a pas d'appels HTTP en attente
  });

  // Test d'intégration : Récupération d'un utilisateur par ID (succès)
  it('should fetch user by id successfully', () => {
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  // Test d'intégration : Récupération d'un utilisateur par ID (erreur 404)
  it('should handle 404 error when fetching user by id', () => {
    service.getById('999').subscribe({
      next: () => fail('Expected error'),
      error: (error) => {
        expect(error.status).toBe(404);
      }
    });

    const req = httpTestingController.expectOne('api/user/999');
    expect(req.request.method).toBe('GET');
    req.flush({}, { status: 404, statusText: 'Not Found' });
  });

  // Test d'intégration : Suppression d'un utilisateur (succès)
  it('should delete user successfully', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeDefined();
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  // Test d'intégration : Suppression d'un utilisateur (erreur 403)
  it('should handle 403 error when deleting user', () => {
    service.delete('1').subscribe({
      next: () => fail('Expected error'),
      error: (error) => {
        expect(error.status).toBe(403);
      }
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({}, { status: 403, statusText: 'Forbidden' });
  });

  // Test d'intégration : Gestion des utilisateurs admin
  it('should handle admin users correctly', () => {
    const adminUser = {
      ...mockUser,
      admin: true
    };

    service.getById('1').subscribe(user => {
      expect(user).toEqual(adminUser);
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(adminUser);
  });

  // Test d'intégration : Gestion des utilisateurs avec des emails complexes
  it('should handle users with complex email addresses', () => {
    const userWithComplexEmail = {
      ...mockUser,
      email: 'user.name+tag@example-domain.co.uk'
    };

    service.getById('1').subscribe(user => {
      expect(user).toEqual(userWithComplexEmail);
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(userWithComplexEmail);
  });

  // Test d'intégration : Gestion des utilisateurs avec des noms longs
  it('should handle users with long names correctly', () => {
    const userWithLongNames = {
      ...mockUser,
      firstName: 'A'.repeat(100),
      lastName: 'B'.repeat(100)
    };

    service.getById('1').subscribe(user => {
      expect(user).toEqual(userWithLongNames);
    });

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(userWithLongNames);
  });
});
