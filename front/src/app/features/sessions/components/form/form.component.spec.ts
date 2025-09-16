import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';

import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
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
    users: [],
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

  beforeEach(async () => {
    const sessionServiceSpy = {
      sessionInformation: {
        admin: true,
        id: 1
      }
    };

    const sessionApiServiceSpy = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      create: jest.fn().mockReturnValue(of(mockSession)),
      update: jest.fn().mockReturnValue(of(mockSession))
    };

    const teacherServiceSpy = {
      all: jest.fn().mockReturnValue(of([mockTeacher]))
    };

    const routerSpy = {
      navigate: jest.fn(),
      url: '/sessions/create'
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
      declarations: [FormComponent],
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        MatSnackBarModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: SessionApiService, useValue: sessionApiServiceSpy },
        { provide: TeacherService, useValue: teacherServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockSessionApiService = TestBed.inject(SessionApiService) as jest.Mocked<SessionApiService>;
    mockTeacherService = TestBed.inject(TeacherService) as jest.Mocked<TeacherService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    mockSnackBar = TestBed.inject(MatSnackBar) as jest.Mocked<MatSnackBar>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form for create mode', () => {
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.get('name')?.value).toBe('');
  });

  it('should initialize form for update mode', () => {
    // Simulate update mode
    Object.defineProperty(component, 'router', {
      value: { url: '/sessions/update/1' },
      writable: true
    });
    
    component.ngOnInit();
    
    expect(component.onUpdate).toBe(true);
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should redirect non-admin users', () => {
    mockSessionService.sessionInformation!.admin = false;
    
    component.ngOnInit();
    
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should submit form for create', () => {
    component.sessionForm?.patchValue({
      name: 'New Session',
      date: '2024-01-01',
      teacher_id: 1,
      description: 'New Description'
    });

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should submit form for update', () => {
    component.onUpdate = true;
    component['id'] = '1';
    component.sessionForm?.patchValue({
      name: 'Updated Session',
      date: '2024-01-01',
      teacher_id: 1,
      description: 'Updated Description'
    });

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', expect.any(Object));
    expect(mockSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should validate required fields', () => {
    const nameControl = component.sessionForm?.get('name');
    const dateControl = component.sessionForm?.get('date');
    const teacherControl = component.sessionForm?.get('teacher_id');
    const descriptionControl = component.sessionForm?.get('description');

    nameControl?.setValue('');
    expect(nameControl?.hasError('required')).toBe(true);

    dateControl?.setValue('');
    expect(dateControl?.hasError('required')).toBe(true);

    teacherControl?.setValue('');
    expect(teacherControl?.hasError('required')).toBe(true);

    descriptionControl?.setValue('');
    expect(descriptionControl?.hasError('required')).toBe(true);
  });

  it('should validate description max length', () => {
    const descriptionControl = component.sessionForm?.get('description');
    const longDescription = 'a'.repeat(2001);

    descriptionControl?.setValue(longDescription);
    expect(descriptionControl?.hasError('maxlength')).toBe(true);

    descriptionControl?.setValue('Valid description');
    expect(descriptionControl?.valid).toBe(true);
  });
});
