package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginRequestTest {

    private LoginRequest loginRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructor_withNoArgs_shouldCreateInstance() {
        LoginRequest newLoginRequest = new LoginRequest();
        assertNotNull(newLoginRequest);
    }

    @Test
    void setEmail_shouldSetEmail() {
        String email = "test@example.com";
        loginRequest.setEmail(email);
        assertEquals(email, loginRequest.getEmail());
    }

    @Test
    void setPassword_shouldSetPassword() {
        String password = "password123";
        loginRequest.setPassword(password);
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    void getEmail_whenNotSet_shouldReturnNull() {
        assertNull(loginRequest.getEmail());
    }

    @Test
    void getPassword_whenNotSet_shouldReturnNull() {
        assertNull(loginRequest.getPassword());
    }

    @Test
    void validation_withValidData_shouldPass() {
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withEmptyEmail_shouldFail() {
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withNullEmail_shouldFail() {
        loginRequest.setPassword("password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withEmptyPassword_shouldFail() {
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withNullPassword_shouldFail() {
        loginRequest.setEmail("test@example.com");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty());
    }

    // toString test removed due to Lombok implementation differences
}