package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("user@test.com")
                .firstName("User")
                .lastName("Test")
                .password("password")
                .admin(false)
                .build();
    }

    @Test
    void findById_user_exists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.isAdmin(), result.isAdmin());
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_user_not_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1L);

        assertNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_null_id() {
        User result = userService.findById(null);

        assertNull(result);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void delete_user_success() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_user_null_id() {
        userService.delete(null);

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void findById_admin_user() {
        User adminUser = User.builder()
                .id(2L)
                .email("admin@test.com")
                .firstName("Admin")
                .lastName("User")
                .password("password")
                .admin(true)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(adminUser));

        User result = userService.findById(2L);

        assertNotNull(result);
        assertEquals(adminUser.getId(), result.getId());
        assertEquals(adminUser.getEmail(), result.getEmail());
        assertTrue(result.isAdmin());
        verify(userRepository).findById(2L);
    }
}
