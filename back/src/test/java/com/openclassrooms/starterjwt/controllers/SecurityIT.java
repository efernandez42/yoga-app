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

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SecurityIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;
    private String invalidToken;

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

        // Create invalid token
        invalidToken = "invalid.jwt.token";
    }

    @Test
    void access_protected_endpoint_without_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_protected_endpoint_with_invalid_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_protected_endpoint_with_malformed_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer malformed.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_protected_endpoint_with_empty_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_protected_endpoint_without_bearer_prefix_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void admin_can_access_admin_endpoints() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void user_cannot_access_admin_endpoints() throws Exception {
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
    void user_can_access_user_endpoints() throws Exception {
        mvc.perform(get("/api/user/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void admin_can_access_user_endpoints() throws Exception {
        mvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void user_cannot_access_other_user_endpoints() throws Exception {
        mvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void user_cannot_delete_other_user() throws Exception {
        mvc.perform(delete("/api/user/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }




    @Test
    void access_with_corrupted_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer corrupted.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_without_authorization_header_unauthorized() throws Exception {
        mvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_wrong_authorization_header_format_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Basic " + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_multiple_authorization_headers_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + adminToken)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void access_with_authorization_header_but_no_token_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_empty_bearer_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_whitespace_only_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer   "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_tab_only_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer\t"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_newline_only_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer\n"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_carriage_return_only_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer\r"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_mixed_whitespace_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer \t\n\r "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_special_characters_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer !@#$%^&*()"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_numbers_only_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer 123456789"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_letters_only_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer abcdefghijklmnopqrstuvwxyz"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_mixed_case_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + adminToken.toUpperCase()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_lowercase_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "bearer " + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_uppercase_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "BEARER " + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_mixed_case_bearer_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "BeArEr " + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_no_space_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer" + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_multiple_spaces_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer  " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void access_with_authorization_header_but_tab_separator_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer\t" + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_newline_separator_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer\n" + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_carriage_return_separator_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer\r" + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_authorization_header_but_mixed_separators_unauthorized() throws Exception {
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer \t\n\r " + adminToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void access_with_expired_token_unauthorized() throws Exception {
        // Simuler un token expiré (format JWT valide mais expiré)
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6MTUxNjI0MjYyMn0.invalid_signature";
        
        mvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }
}
