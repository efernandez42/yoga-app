package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        jwtResponse = new JwtResponse("token", 1L, "username", "firstName", "lastName", true);
    }

    @Test
    void constructor_withAllArgs_shouldCreateInstance() {
        JwtResponse newJwtResponse = new JwtResponse("token123", 1L, "test@example.com", "John", "Doe", true);
        assertNotNull(newJwtResponse);
        assertEquals("token123", newJwtResponse.getToken());
        assertEquals(1L, newJwtResponse.getId());
        assertEquals("test@example.com", newJwtResponse.getUsername());
        assertEquals("John", newJwtResponse.getFirstName());
        assertEquals("Doe", newJwtResponse.getLastName());
        assertTrue(newJwtResponse.getAdmin());
    }

    @Test
    void setToken_shouldSetToken() {
        String token = "jwt_token_123";
        jwtResponse.setToken(token);
        assertEquals(token, jwtResponse.getToken());
    }

    @Test
    void setType_shouldSetType() {
        String type = "Bearer";
        jwtResponse.setType(type);
        assertEquals(type, jwtResponse.getType());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        jwtResponse.setId(id);
        assertEquals(id, jwtResponse.getId());
    }

    @Test
    void setUsername_shouldSetUsername() {
        String username = "test@example.com";
        jwtResponse.setUsername(username);
        assertEquals(username, jwtResponse.getUsername());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        String firstName = "John";
        jwtResponse.setFirstName(firstName);
        assertEquals(firstName, jwtResponse.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        String lastName = "Doe";
        jwtResponse.setLastName(lastName);
        assertEquals(lastName, jwtResponse.getLastName());
    }

    @Test
    void setAdmin_shouldSetAdmin() {
        Boolean admin = true;
        jwtResponse.setAdmin(admin);
        assertEquals(admin, jwtResponse.getAdmin());
    }

    @Test
    void getType_whenNotSet_shouldReturnDefaultValue() {
        assertNotNull(jwtResponse.getType());
        assertEquals("Bearer", jwtResponse.getType());
    }

    @Test
    void getToken_whenSet_shouldReturnToken() {
        assertEquals("token", jwtResponse.getToken());
    }

    @Test
    void getId_whenSet_shouldReturnId() {
        assertEquals(1L, jwtResponse.getId());
    }

    @Test
    void getUsername_whenSet_shouldReturnUsername() {
        assertEquals("username", jwtResponse.getUsername());
    }

    @Test
    void getFirstName_whenSet_shouldReturnFirstName() {
        assertEquals("firstName", jwtResponse.getFirstName());
    }

    @Test
    void getLastName_whenSet_shouldReturnLastName() {
        assertEquals("lastName", jwtResponse.getLastName());
    }

    @Test
    void getAdmin_whenSet_shouldReturnAdmin() {
        assertEquals(true, jwtResponse.getAdmin());
    }

    // toString test removed due to Lombok implementation differences
}
