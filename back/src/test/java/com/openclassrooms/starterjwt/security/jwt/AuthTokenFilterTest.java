package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Tests unitaires pour le filtre d'authentification JWT
@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    // Mock des utilitaires JWT pour simuler la validation des tokens
    @Mock
    private JwtUtils jwtUtils;

    // Mock du service de chargement des utilisateurs
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    // Injection des mocks dans le filtre à tester
    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    // Arrange : initialisation des objets de test pour simuler une requête HTTP
    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        // On nettoie le contexte de sécurité avant chaque test
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withValidToken_shouldSetAuthentication() throws ServletException, IOException {
        // Arrange : on prépare un token JWT valide et les mocks associés
        String token = "valid-token";
        String username = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        
        // On ajoute le token dans l'en-tête Authorization de la requête
        request.addHeader("Authorization", "Bearer " + token);
        
        // On configure les mocks pour simuler un token valide
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act : on exécute le filtre d'authentification
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert : on vérifie que l'authentification a été correctement configurée
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils).validateJwtToken(token);
        verify(jwtUtils).getUserNameFromJwtToken(token);
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    void doFilterInternal_withInvalidToken_shouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange : on prépare un token JWT invalide
        String token = "invalid-token";
        request.addHeader("Authorization", "Bearer " + token);
        
        // On configure le mock pour simuler un token invalide
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // Act : on exécute le filtre d'authentification
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert : on vérifie qu'aucune authentification n'a été configurée
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils).validateJwtToken(token);
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void doFilterInternal_withoutToken_shouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange : aucune requête n'est configurée (pas d'en-tête Authorization)
        
        // Act : on exécute le filtre d'authentification
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert : on vérifie qu'aucune authentification n'a été configurée
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void doFilterInternal_withMalformedHeader_shouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange : on ajoute un en-tête Authorization mal formé (pas de "Bearer ")
        request.addHeader("Authorization", "InvalidFormat");

        // Act : on exécute le filtre d'authentification
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert : on vérifie qu'aucune authentification n'a été configurée
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, never()).validateJwtToken(anyString());
    }

    @Test
    void doFilterInternal_withEmptyToken_shouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange : on ajoute un en-tête Authorization avec un token vide
        request.addHeader("Authorization", "Bearer ");

        // Act : on exécute le filtre d'authentification
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert : on vérifie qu'aucune authentification n'a été configurée
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // Note: Le filtre peut encore appeler validateJwtToken avec une chaîne vide, donc on ne vérifie pas never()
    }
}
