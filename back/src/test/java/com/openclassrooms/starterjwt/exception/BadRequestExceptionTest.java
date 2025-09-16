package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void constructor_noArgs_shouldCreateException() {
        BadRequestException exception = new BadRequestException();
        
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void exception_shouldBeRuntimeException() {
        BadRequestException exception = new BadRequestException();
        
        assertTrue(exception instanceof RuntimeException);
    }
}
