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

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockUserService: jest.Mocked<UserService>;
  let mockRouter: jest.Mocked<Router>;
  let mockSnackBar: jest.Mocked<MatSnackBar>;

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

  beforeEach(async () => {
    const sessionServiceSpy = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      logOut: jest.fn()
    };

    const userServiceSpy = {
      getById: jest.fn(),
      delete: jest.fn()
    };

    const routerSpy = {
      navigate: jest.fn()
    };

    const snackBarSpy = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
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

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockUserService = TestBed.inject(UserService) as jest.Mocked<UserService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
    mockSnackBar = TestBed.inject(MatSnackBar) as jest.Mocked<MatSnackBar>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user data on init', () => {
    mockUserService.getById.mockReturnValue(of(mockUser));

    component.ngOnInit();

    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should call window.history.back when back is called', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});

    component.back();

    expect(historySpy).toHaveBeenCalled();
    
    historySpy.mockRestore();
  });

  it('should delete user and navigate to home when delete is called', () => {
    mockUserService.delete.mockReturnValue(of({}));

    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
