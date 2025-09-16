import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: jest.Mocked<AuthService>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: jest.Mocked<Router>;

  const mockSessionInfo: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(async () => {
    const authServiceSpy = {
      login: jest.fn()
    };

    const sessionServiceSpy = {
      logIn: jest.fn()
    };

    const routerSpy = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: Router, useValue: routerSpy }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    mockAuthService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.form.get('email')?.value).toBe('');
    expect(component.form.get('password')?.value).toBe('');
    expect(component.hide).toBe(true);
    expect(component.onError).toBe(false);
  });

  it('should validate email field', () => {
    const emailControl = component.form.get('email');
    
    emailControl?.setValue('');
    expect(emailControl?.hasError('required')).toBe(true);
    
    emailControl?.setValue('invalid-email');
    expect(emailControl?.hasError('email')).toBe(true);
    
    emailControl?.setValue('valid@email.com');
    expect(emailControl?.valid).toBe(true);
  });

  it('should validate password field', () => {
    const passwordControl = component.form.get('password');
    
    passwordControl?.setValue('');
    expect(passwordControl?.hasError('required')).toBe(true);
    
    passwordControl?.setValue('12');
    expect(passwordControl?.hasError('minlength')).toBe(true);
    
    passwordControl?.setValue('123');
    expect(passwordControl?.valid).toBe(true);
  });

  it('should submit form and handle successful login', () => {
    mockAuthService.login.mockReturnValue(of(mockSessionInfo));
    component.form.patchValue({
      email: 'test@test.com',
      password: 'password123'
    });

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({
      email: 'test@test.com',
      password: 'password123'
    });
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should handle login error', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Login failed')));
    component.form.patchValue({
      email: 'test@test.com',
      password: 'wrongpassword'
    });

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalled();
    expect(component.onError).toBe(true);
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });
});
