package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Tests unitaires pour le DTO UserDto (objet de transfert de données)
class UserDtoTest {

    private UserDto userDto;

    // Arrange : initialisation des données de test communes
    @BeforeEach
    void setUp() {
        userDto = new UserDto();
    }

    @Test
    void constructor_noArgs_shouldCreateInstance() {
        // Act : on crée une instance avec le constructeur par défaut
        UserDto dto = new UserDto();
        
        // Assert : on vérifie que l'instance est créée correctement
        assertNotNull(dto);
    }

    @Test
    void constructor_withAllArgs_shouldCreateInstance() {
        // Arrange : on prépare les données pour le constructeur complet
        LocalDateTime now = LocalDateTime.now();
        
        // Act : on crée une instance avec tous les paramètres
        UserDto dto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", now, now);
        
        // Assert : on vérifie que tous les champs sont correctement initialisés
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(true, dto.isAdmin());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void getId_shouldReturnId() {
        Long id = 1L;
        userDto.setId(id);
        assertEquals(id, userDto.getId());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        userDto.setId(id);
        assertEquals(id, userDto.getId());
    }

    @Test
    void getEmail_shouldReturnEmail() {
        String email = "test@example.com";
        userDto.setEmail(email);
        assertEquals(email, userDto.getEmail());
    }

    @Test
    void setEmail_shouldSetEmail() {
        String email = "test@example.com";
        userDto.setEmail(email);
        assertEquals(email, userDto.getEmail());
    }

    @Test
    void getFirstName_shouldReturnFirstName() {
        String firstName = "John";
        userDto.setFirstName(firstName);
        assertEquals(firstName, userDto.getFirstName());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        String firstName = "John";
        userDto.setFirstName(firstName);
        assertEquals(firstName, userDto.getFirstName());
    }

    @Test
    void getLastName_shouldReturnLastName() {
        String lastName = "Doe";
        userDto.setLastName(lastName);
        assertEquals(lastName, userDto.getLastName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        String lastName = "Doe";
        userDto.setLastName(lastName);
        assertEquals(lastName, userDto.getLastName());
    }

    @Test
    void isAdmin_shouldReturnAdmin() {
        boolean admin = true;
        userDto.setAdmin(admin);
        assertEquals(admin, userDto.isAdmin());
    }

    @Test
    void setAdmin_shouldSetAdmin() {
        boolean admin = true;
        userDto.setAdmin(admin);
        assertEquals(admin, userDto.isAdmin());
    }

    @Test
    void getCreatedAt_shouldReturnCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        userDto.setCreatedAt(createdAt);
        assertEquals(createdAt, userDto.getCreatedAt());
    }

    @Test
    void setCreatedAt_shouldSetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        userDto.setCreatedAt(createdAt);
        assertEquals(createdAt, userDto.getCreatedAt());
    }

    @Test
    void getUpdatedAt_shouldReturnUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        userDto.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, userDto.getUpdatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        userDto.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, userDto.getUpdatedAt());
    }
}
