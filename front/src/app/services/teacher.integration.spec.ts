// Imports nécessaires pour les tests d'intégration
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

/**
 * Tests d'intégration pour le service TeacherService
 * Ces tests vérifient les appels HTTP réels avec HttpClientTestingModule
 */
describe('TeacherService Integration Tests', () => {
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  // Données de test
  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  // Configuration avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Module de test pour les appels HTTP
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  // Nettoyage après chaque test
  afterEach(() => {
    httpTestingController.verify(); // Vérifie qu'il n'y a pas d'appels HTTP en attente
  });

  // Test d'intégration : Récupération de tous les enseignants (succès)
  it('should fetch all teachers successfully', () => {
    const mockTeachers = [mockTeacher];

    service.all().subscribe(teachers => {
      expect(teachers).toEqual(mockTeachers);
    });

    const req = httpTestingController.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  // Test d'intégration : Récupération de tous les enseignants (erreur)
  it('should handle error when fetching all teachers', () => {
    service.all().subscribe({
      next: () => fail('Expected error'),
      error: (error) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpTestingController.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush({}, { status: 500, statusText: 'Internal Server Error' });
  });

  // Test d'intégration : Récupération d'un enseignant par ID (succès)
  it('should fetch teacher detail successfully', () => {
    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpTestingController.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });

  // Test d'intégration : Récupération d'un enseignant par ID (erreur 404)
  it('should handle 404 error when fetching teacher detail', () => {
    service.detail('999').subscribe({
      next: () => fail('Expected error'),
      error: (error) => {
        expect(error.status).toBe(404);
      }
    });

    const req = httpTestingController.expectOne('api/teacher/999');
    expect(req.request.method).toBe('GET');
    req.flush({}, { status: 404, statusText: 'Not Found' });
  });

  // Test d'intégration : Gestion des enseignants avec des noms longs
  it('should handle teachers with long names correctly', () => {
    const teacherWithLongName = {
      ...mockTeacher,
      firstName: 'A'.repeat(50),
      lastName: 'B'.repeat(50)
    };

    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(teacherWithLongName);
    });

    const req = httpTestingController.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(teacherWithLongName);
  });

  // Test d'intégration : Gestion des enseignants avec des caractères spéciaux
  it('should handle teachers with special characters in names', () => {
    const teacherWithSpecialChars = {
      ...mockTeacher,
      firstName: 'Jean-Pierre',
      lastName: "O'Connor-Smith"
    };

    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(teacherWithSpecialChars);
    });

    const req = httpTestingController.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(teacherWithSpecialChars);
  });
});
