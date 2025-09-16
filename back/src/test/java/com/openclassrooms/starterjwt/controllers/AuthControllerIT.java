package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Configuration des tests d'intégration avec Spring Boot
@SpringBootTest
@AutoConfigureMockMvc
// Chargement des données de test avant chaque test pour avoir un état connu
@Sql(scripts = "/sql/script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthControllerIT {

    // MockMvc permet de simuler des requêtes HTTP sans démarrer un serveur complet
    @Autowired
    private MockMvc mvc;

    // ObjectMapper pour convertir les objets Java en JSON et vice versa
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_admin_ok() throws Exception {
        // Arrange : on prépare les données de connexion pour un administrateur
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Act : on envoie une requête POST de connexion
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // Assert : on vérifie que la connexion réussit et retourne les bonnes données
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("yoga@studio.com"))
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void login_bad_credentials_unauthorized() throws Exception {
        // Arrange : on prépare des données de connexion avec un mot de passe incorrect
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("wrong");

        // Act : on tente de se connecter avec de mauvais identifiants
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // Assert : on vérifie que la connexion échoue avec le statut 401 (Unauthorized)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_user_not_found() throws Exception {
        // Arrange : on prépare des données de connexion avec un email inexistant
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@studio.com");
        loginRequest.setPassword("test!1234");

        // Act : on tente de se connecter avec un utilisateur qui n'existe pas
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // Assert : on vérifie que la connexion échoue avec le statut 401 (utilisateur non trouvé)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_regular_user_ok() throws Exception {
        // Arrange : on prépare les données de connexion pour un utilisateur normal (non admin)
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@studio.com");
        loginRequest.setPassword("test!1234");

        // Act : on envoie une requête POST de connexion
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                // Assert : on vérifie que la connexion réussit et retourne les bonnes données utilisateur
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("user@studio.com"))
                .andExpect(jsonPath("$.firstName").value("Regular"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    void register_user_success() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void register_user_email_already_taken() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

    @Test
    void login_invalid_json() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_email_not_found_unauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("test!1234");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_empty_email_bad_request() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("test!1234");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_empty_password_bad_request() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_null_email_bad_request() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("test!1234");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_null_password_bad_request() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_invalid_email_format_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_password_too_short_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("123");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_first_name_too_short_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("Jo");
        signupRequest.setLastName("User");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_last_name_too_short_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Do");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_empty_email_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_empty_password_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_empty_first_name_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("");
        signupRequest.setLastName("Doe");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_empty_last_name_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_null_email_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_null_password_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_null_first_name_bad_request() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setLastName("Doe");

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_null_last_name_bad_request() throws Exception {
        // Arrange : on prépare des données d'inscription avec un nom de famille manquant (null)
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("test!1234");
        signupRequest.setFirstName("John");
        // lastName n'est pas défini (null)

        // Act : on tente de s'inscrire avec des données incomplètes
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                // Assert : on vérifie que la validation échoue avec le statut 400 (Bad Request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_invalid_json_bad_request() throws Exception {
        // Arrange : on prépare un JSON mal formé (syntaxe invalide)
        // Act : on envoie une requête avec un JSON invalide
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                // Assert : on vérifie que le parsing JSON échoue avec le statut 400
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_without_content_type_unsupported_media_type() throws Exception {
        // Arrange : on prépare des données de connexion valides
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Act : on envoie une requête sans Content-Type (le serveur ne peut pas parser le JSON)
        mvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(loginRequest)))
                // Assert : on vérifie que le serveur refuse le format avec le statut 415 (Unsupported Media Type)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void login_malformed_json_bad_request() throws Exception {
        // Arrange : on prépare un JSON mal formé (accolades mal placées)
        // Act : on envoie une requête avec un JSON syntaxiquement incorrect
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                // Assert : on vérifie que le parsing JSON échoue avec le statut 400
                .andExpect(status().isBadRequest());
    }
}
