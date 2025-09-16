import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    date: new Date(),
    teacher_id: 1,
    description: 'Test Description',
    users: [1, 2, 3],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    const mockSessions = [mockSession];
    
    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should get session detail', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete session', () => {
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create session', () => {
    service.create(mockSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

  it('should update session', () => {
    service.update('1', mockSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

  it('should participate in session', () => {
    service.participate('1', '2').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should unparticipate from session', () => {
    service.unParticipate('1', '2').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
