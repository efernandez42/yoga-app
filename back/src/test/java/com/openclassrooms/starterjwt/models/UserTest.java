package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Tests unitaires pour le modèle User (entité JPA)
@ExtendWith(MockitoExtension.class)
class UserTest {

    private User user;
    private LocalDateTime now;

    // Arrange : initialisation des données de test communes
    @BeforeEach
    void setUp() {
        user = new User();
        now = LocalDateTime.now();
    }

    @Test
    void constructor_withNoArgs_shouldCreateInstance() {
        // Act : on crée une instance avec le constructeur par défaut
        User newUser = new User();
        
        // Assert : on vérifie que l'instance est créée correctement
        assertNotNull(newUser);
    }

    @Test
    void constructor_withAllArgs_shouldCreateInstance() {
        // Act : on crée une instance avec tous les paramètres
        User newUser = new User(1L, "test@example.com", "Doe", "John", "password", true, now, now);
        
        // Assert : on vérifie que tous les champs sont correctement initialisés
        assertNotNull(newUser);
        assertEquals(1L, newUser.getId());
        assertEquals("test@example.com", newUser.getEmail());
        assertEquals("Doe", newUser.getLastName());
        assertEquals("John", newUser.getFirstName());
        assertEquals("password", newUser.getPassword());
        assertTrue(newUser.isAdmin());
        assertEquals(now, newUser.getCreatedAt());
        assertEquals(now, newUser.getUpdatedAt());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void setEmail_shouldSetEmail() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    void setLastName_shouldSetLastName() {
        String lastName = "Doe";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        String firstName = "John";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void setPassword_shouldSetPassword() {
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void setAdmin_shouldSetAdmin() {
        boolean admin = true;
        user.setAdmin(admin);
        assertEquals(admin, user.isAdmin());
    }

    @Test
    void setCreatedAt_shouldSetCreatedAt() {
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetUpdatedAt() {
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdatedAt());
    }

    // Note: User model doesn't have sessions relationship in this implementation

    @Test
    void equals_withSameId_shouldReturnTrue() {
        // Arrange : on crée deux utilisateurs avec le même ID
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        
        // Act & Assert : on vérifie que les utilisateurs sont considérés comme égaux (même ID)
        assertEquals(user1, user2);
    }

    @Test
    void equals_withDifferentId_shouldReturnFalse() {
        // Arrange : on crée deux utilisateurs avec des IDs différents
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        
        // Act & Assert : on vérifie que les utilisateurs ne sont pas égaux (IDs différents)
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_withNull_shouldReturnFalse() {
        // Arrange : on crée un utilisateur et on le compare avec null
        User user1 = new User();
        user1.setId(1L);
        
        // Act & Assert : on vérifie que la comparaison avec null retourne false
        assertNotEquals(user1, null);
    }

    @Test
    void equals_withSameObject_shouldReturnTrue() {
        // Arrange : on crée un utilisateur
        User user1 = new User();
        user1.setId(1L);
        
        // Act & Assert : on vérifie qu'un objet est égal à lui-même (réflexivité)
        assertEquals(user1, user1);
    }

    @Test
    void hashCode_withSameId_shouldReturnSameHashCode() {
        // Arrange : on crée deux utilisateurs avec le même ID
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        
        // Act & Assert : on vérifie que les hash codes sont identiques (nécessaire pour equals)
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        
        String result = user.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}
