package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void constructor_noArgs_shouldCreateException() {
        NotFoundException exception = new NotFoundException();
        
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void exception_shouldBeRuntimeException() {
        NotFoundException exception = new NotFoundException();
        
        assertTrue(exception instanceof RuntimeException);
    }
}
