import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';

import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockRouter: jest.Mocked<Router>;
  let mockSnackBar: jest.Mocked<MatSnackBar>;

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

  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  beforeEach(async () => {
    const sessionApiServiceSpy = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of(undefined)),
      unParticipate: jest.fn().mockReturnValue(of(undefined))
    };

    const teacherServiceSpy = {
      detail: jest.fn().mockReturnValue(of(mockTeacher))
    };

    const routerSpy = {
      navigate: jest.fn()
    };

    const snackBarSpy = {
      open: jest.fn()
    };

    const mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    };

        await TestBed.configureTestingModule({
          imports: [
            RouterTestingModule,
            HttpClientModule,
            MatSnackBarModule,
            MatCardModule,
            MatIconModule,
            MatButtonModule,
            ReactiveFormsModule
          ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: sessionApiServiceSpy },
        { provide: TeacherService, useValue: teacherServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
    })
      .compileComponents();
    
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    mockSessionApiService = TestBed.inject(SessionApiService) as jest.Mocked<SessionApiService>;
    mockTeacherService = TestBed.inject(TeacherService) as jest.Mocked<TeacherService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    mockSnackBar = TestBed.inject(MatSnackBar) as jest.Mocked<MatSnackBar>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with correct values', () => {
    expect(component.sessionId).toBe('1');
    expect(component.isAdmin).toBe(true);
    expect(component.userId).toBe('1');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
    expect(component.isParticipate).toBe(true);
  });

  it('should call fetchSession on init', () => {
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
    component.ngOnInit();
    expect(fetchSessionSpy).toHaveBeenCalled();
  });

  it('should call window.history.back when back is called', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(historySpy).toHaveBeenCalled();
    historySpy.mockRestore();
  });

  it('should delete session and navigate to sessions', () => {
    mockSessionApiService.delete.mockReturnValue(of({}));
    
    component.delete();
    
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate in session', () => {
    mockSessionApiService.participate.mockReturnValue(of(undefined));
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
    
    component.participate();
    
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
    expect(fetchSessionSpy).toHaveBeenCalled();
  });

  it('should unparticipate from session', () => {
    mockSessionApiService.unParticipate.mockReturnValue(of(undefined));
    const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
    
    component.unParticipate();
    
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    expect(fetchSessionSpy).toHaveBeenCalled();
  });

  it('should fetch session and teacher data', () => {
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));
    
    (component as any).fetchSession();
    
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.session).toEqual(mockSession);
    expect(component.isParticipate).toBe(true); // user id 1 is in users array
    expect(mockTeacherService.detail).toHaveBeenCalledWith('1');
  });

  it('should set isParticipate to false when user is not in session users', () => {
    const sessionWithoutUser = { ...mockSession, users: [2, 3] };
    mockSessionApiService.detail.mockReturnValue(of(sessionWithoutUser));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));
    
    (component as any).fetchSession();
    
    expect(component.isParticipate).toBe(false);
  });
});

