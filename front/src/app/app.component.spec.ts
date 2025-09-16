import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './services/session.service';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: any;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: jest.Mocked<Router>;

  beforeEach(async () => {
    const sessionServiceSpy = {
      $isLogged: jest.fn(),
      logOut: jest.fn()
    };

    const routerSpy = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: AuthService, useValue: {} }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    mockSessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
    mockRouter = TestBed.inject(Router) as jest.Mocked<Router>;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should call sessionService.$isLogged when $isLogged is called', () => {
    const mockObservable = of(true);
    mockSessionService.$isLogged.mockReturnValue(mockObservable);

    const result = component.$isLogged();

    expect(mockSessionService.$isLogged).toHaveBeenCalled();
    expect(result).toBe(mockObservable);
  });

  it('should logout and navigate to home when logout is called', () => {
    component.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
