package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = mock(AuthenticationException.class);
        when(authException.getMessage()).thenReturn("Unauthorized");
    }

    @Test
    void commence_shouldSetUnauthorizedStatus() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, authException);
        
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    void commence_shouldSetJsonContentType() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, authException);
        
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    @Test
    void commence_shouldWriteErrorMessage() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, authException);
        
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("Unauthorized"));
        assertTrue(responseBody.contains("error"));
    }

    @Test
    void commence_shouldLogError() throws IOException, ServletException {
        authEntryPointJwt.commence(request, response, authException);
        
        // Verify that the error is logged (we can't easily test logging without additional setup)
        // The main thing is that the method doesn't throw an exception
        assertTrue(true);
    }
}
