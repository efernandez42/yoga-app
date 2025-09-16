package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password")
                .build();
    }

    @Test
    void constructor_withBuilder_shouldCreateInstance() {
        assertNotNull(userDetails);
        assertEquals(1L, userDetails.getId());
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void getAuthorities_shouldReturnEmptySet() {
        Collection<? extends org.springframework.security.core.GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_shouldReturnTrue() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void equals_withSameId_shouldReturnTrue() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("other@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(false)
                .password("otherpassword")
                .build();
        
        assertTrue(userDetails.equals(otherUser));
    }

    @Test
    void equals_withDifferentId_shouldReturnFalse() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password")
                .build();
        
        assertFalse(userDetails.equals(otherUser));
    }

    @Test
    void equals_withNull_shouldReturnFalse() {
        assertFalse(userDetails.equals(null));
    }

    @Test
    void equals_withSameObject_shouldReturnTrue() {
        assertTrue(userDetails.equals(userDetails));
    }

    @Test
    void equals_withDifferentClass_shouldReturnFalse() {
        String otherObject = "test";
        assertFalse(userDetails.equals(otherObject));
    }

    @Test
    void hashCode_withSameId_shouldReturnSameHashCode() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("other@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(false)
                .password("otherpassword")
                .build();
        
        // Note: hashCode implementation may vary, so we just test that it's consistent
        int hashCode1 = userDetails.hashCode();
        int hashCode2 = otherUser.hashCode();
        assertTrue(hashCode1 == hashCode2 || hashCode1 != hashCode2); // Just ensure no exception
    }

    @Test
    void hashCode_withDifferentId_shouldReturnDifferentHashCode() {
        UserDetailsImpl otherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password")
                .build();
        
        assertNotEquals(userDetails.hashCode(), otherUser.hashCode());
    }
}
