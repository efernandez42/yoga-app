package com.openclassrooms.starterjwt.controllers;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/script.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

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
    void findById_user_exists_ok() throws Exception {
        mvc.perform(get("/api/user/2")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("user@studio.com"))
                .andExpect(jsonPath("$.firstName").value("Regular"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(false))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void findById_user_not_found() throws Exception {
        mvc.perform(get("/api/user/999")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_invalid_id_bad_request() throws Exception {
        mvc.perform(get("/api/user/invalid")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_user_own_account_ok() throws Exception {
        // First create a new user to delete
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"temp@studio.com\",\"password\":\"test!1234\",\"firstName\":\"Temp\",\"lastName\":\"User\"}"))
                .andExpect(status().isOk());

        // Login as the new user
        LoginRequest tempLogin = new LoginRequest();
        tempLogin.setEmail("temp@studio.com");
        tempLogin.setPassword("test!1234");

        MvcResult tempResult = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String tempResponse = tempResult.getResponse().getContentAsString();
        String tempToken = objectMapper.readTree(tempResponse).get("token").asText();
        Long tempUserId = objectMapper.readTree(tempResponse).get("id").asLong();

        // Delete own account
        mvc.perform(delete("/api/user/" + tempUserId)
                .header("Authorization", "Bearer " + tempToken))
                .andExpect(status().isOk());
    }

    @Test
    void delete_user_other_account_unauthorized() throws Exception {
        mvc.perform(delete("/api/user/3")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_user_not_found() throws Exception {
        mvc.perform(delete("/api/user/999")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_user_invalid_id_bad_request() throws Exception {
        mvc.perform(delete("/api/user/invalid")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void access_without_token_unauthorized() throws Exception {
        mvc.perform(get("/api/user/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_admin_can_access_any_user() throws Exception {
        mvc.perform(get("/api/user/2")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("user@studio.com"));
    }

    @Test
    void findById_admin_can_access_own_account() throws Exception {
        mvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("yoga@studio.com"))
                .andExpect(jsonPath("$.admin").value(true));
    }


    @Test
    void findById_invalid_user_id_format() throws Exception {
        mvc.perform(get("/api/user/abc")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_invalid_user_id_format() throws Exception {
        mvc.perform(delete("/api/user/abc")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_negative_user_id() throws Exception {
        mvc.perform(get("/api/user/-1")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_negative_user_id() throws Exception {
        mvc.perform(delete("/api/user/-1")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_zero_user_id() throws Exception {
        mvc.perform(get("/api/user/0")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_zero_user_id() throws Exception {
        mvc.perform(delete("/api/user/0")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_very_large_user_id() throws Exception {
        mvc.perform(get("/api/user/999999999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_very_large_user_id() throws Exception {
        mvc.perform(delete("/api/user/999999999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
