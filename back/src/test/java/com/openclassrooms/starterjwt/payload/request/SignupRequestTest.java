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
class SignupRequestTest {

    private SignupRequest signupRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void constructor_withNoArgs_shouldCreateInstance() {
        SignupRequest newSignupRequest = new SignupRequest();
        assertNotNull(newSignupRequest);
    }

    @Test
    void setEmail_shouldSetEmail() {
        String email = "test@example.com";
        signupRequest.setEmail(email);
        assertEquals(email, signupRequest.getEmail());
    }

    @Test
    void setPassword_shouldSetPassword() {
        String password = "password123";
        signupRequest.setPassword(password);
        assertEquals(password, signupRequest.getPassword());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        String firstName = "John";
        signupRequest.setFirstName(firstName);
        assertEquals(firstName, signupRequest.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        String lastName = "Doe";
        signupRequest.setLastName(lastName);
        assertEquals(lastName, signupRequest.getLastName());
    }

    @Test
    void getEmail_whenNotSet_shouldReturnNull() {
        assertNull(signupRequest.getEmail());
    }

    @Test
    void getPassword_whenNotSet_shouldReturnNull() {
        assertNull(signupRequest.getPassword());
    }

    @Test
    void getFirstName_whenNotSet_shouldReturnNull() {
        assertNull(signupRequest.getFirstName());
    }

    @Test
    void getLastName_whenNotSet_shouldReturnNull() {
        assertNull(signupRequest.getLastName());
    }

    @Test
    void validation_withValidData_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withEmptyEmail_shouldFail() {
        signupRequest.setEmail("");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withNullEmail_shouldFail() {
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withEmptyPassword_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withNullPassword_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withEmptyFirstName_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withNullFirstName_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withEmptyLastName_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withNullLastName_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withInvalidEmail_shouldFail() {
        signupRequest.setEmail("invalid-email");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withEmailTooLong_shouldFail() {
        signupRequest.setEmail("a".repeat(51) + "@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withFirstNameTooShort_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("Jo");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withFirstNameTooLong_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("a".repeat(21));
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withLastNameTooShort_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Do");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withLastNameTooLong_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("a".repeat(21));
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withPasswordTooShort_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("12345");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withPasswordTooLong_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("a".repeat(41));
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withWhitespaceEmail_shouldFail() {
        signupRequest.setEmail("  ");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withWhitespacePassword_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("  ");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withWhitespaceFirstName_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("  ");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withWhitespaceLastName_shouldFail() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("  ");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validation_withExactMinLengthFirstName_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("Joh");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withExactMinLengthLastName_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withExactMinLengthPassword_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("123456");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withExactMaxLengthFirstName_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("a".repeat(20));
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withExactMaxLengthLastName_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("a".repeat(20));
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withExactMaxLengthPassword_shouldPass() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("a".repeat(40));
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withMultipleViolations_shouldFail() {
        signupRequest.setEmail("invalid-email");
        signupRequest.setPassword("123");
        signupRequest.setFirstName("Jo");
        signupRequest.setLastName("Do");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() > 1);
    }

    @Test
    void validation_withAllFieldsNull_shouldFail() {
        signupRequest.setEmail(null);
        signupRequest.setPassword(null);
        signupRequest.setFirstName(null);
        signupRequest.setLastName(null);
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 4);
    }

    @Test
    void validation_withAllFieldsEmpty_shouldFail() {
        signupRequest.setEmail("");
        signupRequest.setPassword("");
        signupRequest.setFirstName("");
        signupRequest.setLastName("");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 4);
    }

    @Test
    void validation_withAllFieldsWhitespace_shouldFail() {
        signupRequest.setEmail("  ");
        signupRequest.setPassword("  ");
        signupRequest.setFirstName("  ");
        signupRequest.setLastName("  ");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 4);
    }

    @Test
    void validation_withBoundaryValues_shouldPass() {
        signupRequest.setEmail("a@b.co");
        signupRequest.setPassword("123456");
        signupRequest.setFirstName("Joh");
        signupRequest.setLastName("Doe");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_withMaxBoundaryValues_shouldPass() {
        signupRequest.setEmail("a".repeat(50) + "@example.com");
        signupRequest.setPassword("a".repeat(40));
        signupRequest.setFirstName("a".repeat(20));
        signupRequest.setLastName("a".repeat(20));
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty()); // This should fail due to email length
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        
        String result = signupRequest.toString();
        assertNotNull(result);
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("password123"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}