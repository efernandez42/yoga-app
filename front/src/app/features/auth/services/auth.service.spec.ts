import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let mockHttpClient: jest.Mocked<HttpClient>;

  const mockLoginRequest: LoginRequest = {
    email: 'test@test.com',
    password: 'password123'
  };

  const mockRegisterRequest: RegisterRequest = {
    email: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    password: 'password123'
  };

  const mockSessionInfo: SessionInformation = {
    token: 'mock-token',
    type: 'Bearer',
    id: 1,
    username: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(() => {
    const httpClientSpy = {
      post: jest.fn()
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: HttpClient, useValue: httpClientSpy }
      ]
    });
    
    service = TestBed.inject(AuthService);
    mockHttpClient = TestBed.inject(HttpClient) as jest.Mocked<HttpClient>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register user', () => {
    mockHttpClient.post.mockReturnValue(of(undefined));

    service.register(mockRegisterRequest).subscribe(result => {
      expect(result).toBeUndefined();
    });

    expect(mockHttpClient.post).toHaveBeenCalledWith(
      'api/auth/register',
      mockRegisterRequest
    );
  });

  it('should login user', () => {
    mockHttpClient.post.mockReturnValue(of(mockSessionInfo));

    service.login(mockLoginRequest).subscribe(result => {
      expect(result).toEqual(mockSessionInfo);
    });

    expect(mockHttpClient.post).toHaveBeenCalledWith(
      'api/auth/login',
      mockLoginRequest
    );
  });
});
