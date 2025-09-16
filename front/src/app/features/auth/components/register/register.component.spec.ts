import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: jest.Mocked<AuthService>;
  let mockRouter: jest.Mocked<Router>;

  beforeEach(async () => {
    const authServiceSpy = {
      register: jest.fn()
    };

    const routerSpy = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    mockAuthService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.form.get('email')?.value).toBe('');
    expect(component.form.get('firstName')?.value).toBe('');
    expect(component.form.get('lastName')?.value).toBe('');
    expect(component.form.get('password')?.value).toBe('');
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

  it('should validate firstName field', () => {
    const firstNameControl = component.form.get('firstName');
    
    firstNameControl?.setValue('');
    expect(firstNameControl?.hasError('required')).toBe(true);
    
    firstNameControl?.setValue('Jo');
    expect(firstNameControl?.hasError('minlength')).toBe(true);
    
    firstNameControl?.setValue('John');
    expect(firstNameControl?.valid).toBe(true);
  });

  it('should validate lastName field', () => {
    const lastNameControl = component.form.get('lastName');
    
    lastNameControl?.setValue('');
    expect(lastNameControl?.hasError('required')).toBe(true);
    
    lastNameControl?.setValue('Do');
    expect(lastNameControl?.hasError('minlength')).toBe(true);
    
    lastNameControl?.setValue('Doe');
    expect(lastNameControl?.valid).toBe(true);
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

  it('should submit form and handle successful registration', () => {
    mockAuthService.register.mockReturnValue(of(undefined));
    component.form.patchValue({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalledWith({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('should handle registration error', () => {
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));
    component.form.patchValue({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalled();
    expect(component.onError).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });
});
