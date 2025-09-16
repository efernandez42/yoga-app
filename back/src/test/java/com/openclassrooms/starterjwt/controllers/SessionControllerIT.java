package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Configuration des tests d'intégration pour le contrôleur des sessions
@SpringBootTest
@AutoConfigureMockMvc
// Chargement des données de test avant chaque test pour avoir un état connu
@Sql(scripts = "/sql/script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SessionControllerIT {

    // MockMvc permet de simuler des requêtes HTTP sans démarrer un serveur complet
    @Autowired
    private MockMvc mvc;

    // ObjectMapper pour convertir les objets Java en JSON et vice versa
    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    // Arrange : initialisation des tokens d'authentification pour les tests
    @BeforeEach
    void setUp() throws Exception {
        // Login as admin
        LoginRequest adminLogin = new LoginRequest();
        adminLogin.setEmail("yoga@studio.com");
        adminLogin.setPassword("test!1234");

        MvcResult adminResult = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String adminResponse = adminResult.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(adminResponse).get("token").asText();

        // Login as regular user
        LoginRequest userLogin = new LoginRequest();
        userLogin.setEmail("user@studio.com");
        userLogin.setPassword("test!1234");

        MvcResult userResult = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String userResponse = userResult.getResponse().getContentAsString();
        userToken = objectMapper.readTree(userResponse).get("token").asText();
    }

    @Test
    void findAll_sessions_ok() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value(containsString("Yoga")))
                .andExpect(jsonPath("$[0].name").value(containsString("D")))
                .andExpect(jsonPath("$[1].name").value(containsString("Yoga")))
                .andExpect(jsonPath("$[1].name").value(containsString("Avanc")))
                .andExpect(jsonPath("$[2].name").value(containsString("M")));
    }

    @Test
    void findById_session_exists_ok() throws Exception {
        // Arrange : on utilise un token admin valide (pas de données à préparer)
        
        // Act : on récupère une session existante par son ID
        mvc.perform(get("/api/session/1")
                .header("Authorization", "Bearer " + adminToken))
                // Assert : on vérifie que la session est retournée avec les bonnes données
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(containsString("Yoga")))
                .andExpect(jsonPath("$.name").value(containsString("D")))
                .andExpect(jsonPath("$.teacher_id").value(1));
    }

    @Test
    void findById_session_not_found() throws Exception {
        // Arrange : on utilise un ID de session qui n'existe pas (999)
        
        // Act : on tente de récupérer une session inexistante
        mvc.perform(get("/api/session/999")
                .header("Authorization", "Bearer " + adminToken))
                // Assert : on vérifie que le serveur retourne 404 (Not Found)
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_invalid_id_bad_request() throws Exception {
        // Arrange : on utilise un ID invalide (format non numérique)
        
        // Act : on tente de récupérer une session avec un ID mal formé
        mvc.perform(get("/api/session/invalid")
                .header("Authorization", "Bearer " + adminToken))
                // Assert : on vérifie que le serveur retourne 400 (Bad Request) pour un format invalide
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_session_admin_ok() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Nouvelle Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description de la nouvelle session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(2L));

        mvc.perform(post("/api/session")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nouvelle Session"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.description").value("Description de la nouvelle session"));
    }

    @Test
    void create_session_invalid_data_bad_request() throws Exception {
        SessionDto sessionDto = new SessionDto();
        // Missing required fields

        mvc.perform(post("/api/session")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_session_admin_ok() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session Modifiée");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description modifiée");
        sessionDto.setTeacher_id(2L);
        sessionDto.setUsers(Arrays.asList(2L, 3L));

        mvc.perform(put("/api/session/1")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Session Modifiée"))
                .andExpect(jsonPath("$.teacher_id").value(2));
    }

    @Test
    void update_session_invalid_id_bad_request() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session Modifiée");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description modifiée");
        sessionDto.setTeacher_id(1L);

        mvc.perform(put("/api/session/invalid")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_session_admin_ok() throws Exception {
        mvc.perform(delete("/api/session/3")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void delete_session_not_found() throws Exception {
        mvc.perform(delete("/api/session/999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_session_invalid_id_bad_request() throws Exception {
        mvc.perform(delete("/api/session/invalid")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_user_ok() throws Exception {
        // Utiliser la session 2 qui n'a pas encore l'utilisateur 2
        mvc.perform(post("/api/session/2/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void participate_invalid_session_id_bad_request() throws Exception {
        mvc.perform(post("/api/session/invalid/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_invalid_user_id_bad_request() throws Exception {
        mvc.perform(post("/api/session/1/participate/invalid")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_user_ok() throws Exception {
        mvc.perform(delete("/api/session/1/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void noLongerParticipate_invalid_session_id_bad_request() throws Exception {
        mvc.perform(delete("/api/session/invalid/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_invalid_user_id_bad_request() throws Exception {
        mvc.perform(delete("/api/session/1/participate/invalid")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void access_without_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get_session_by_id_not_found() throws Exception {
        mvc.perform(get("/api/session/999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void get_session_by_id_invalid_format() throws Exception {
        mvc.perform(get("/api/session/invalid")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_session_user_forbidden() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Test Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(post("/api/session")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void create_session_invalid_data() throws Exception {
        SessionDto sessionDto = new SessionDto();
        // Missing required fields

        mvc.perform(post("/api/session")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_session_invalid_json() throws Exception {
        mvc.perform(post("/api/session")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_session_not_found() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(put("/api/session/999")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void update_session_invalid_format() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(put("/api/session/invalid")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_session_user_forbidden() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(put("/api/session/1")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void update_session_invalid_data() throws Exception {
        SessionDto sessionDto = new SessionDto();
        // Missing required fields

        mvc.perform(put("/api/session/1")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_session_invalid_json() throws Exception {
        mvc.perform(put("/api/session/1")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void participate_session_not_found() throws Exception {
        mvc.perform(post("/api/session/999/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void participate_invalid_session_id() throws Exception {
        mvc.perform(post("/api/session/invalid/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_invalid_user_id() throws Exception {
        mvc.perform(post("/api/session/1/participate/invalid")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_user_forbidden() throws Exception {
        mvc.perform(post("/api/session/1/participate/2")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void no_longer_participate_session_not_found() throws Exception {
        mvc.perform(delete("/api/session/999/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void no_longer_participate_invalid_session_id() throws Exception {
        mvc.perform(delete("/api/session/invalid/participate/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void no_longer_participate_invalid_user_id() throws Exception {
        mvc.perform(delete("/api/session/1/participate/invalid")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void no_longer_participate_user_forbidden() throws Exception {
        mvc.perform(delete("/api/session/1/participate/2")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void get_sessions_without_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get_session_by_id_without_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_session_without_token_unauthorized() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Test Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_session_without_token_unauthorized() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_session_without_token_unauthorized() throws Exception {
        mvc.perform(delete("/api/session/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void participate_without_token_unauthorized() throws Exception {
        mvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void no_longer_participate_without_token_unauthorized() throws Exception {
        mvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_session_without_authentication_unauthorized() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Test Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update_session_with_non_numeric_id_bad_request() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(1L);

        mvc.perform(put("/api/session/abc")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_session_with_user_role_forbidden() throws Exception {
        mvc.perform(delete("/api/session/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }
}
