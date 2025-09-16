package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// Configuration des tests unitaires avec Mockito
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    // Mock du repository des sessions pour simuler l'accès aux données
    @Mock
    private SessionRepository sessionRepository;

    // Mock du repository des utilisateurs pour simuler l'accès aux données
    @Mock
    private UserRepository userRepository;

    // Injection des mocks dans le service à tester
    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;
    private Teacher teacher;

    // Arrange : initialisation des données de test communes à tous les tests
    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        user = User.builder()
                .id(1L)
                .email("user@test.com")
                .firstName("User")
                .lastName("Test")
                .password("password")
                .admin(false)
                .build();

        session = Session.builder()
                .id(1L)
                .name("Test Session")
                .date(new Date())
                .description("Test Description")
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();
    }

    @Test
    void create_session_success() {
        // Arrange : on configure le mock pour retourner la session sauvegardée
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act : on appelle la méthode de création de session
        Session result = sessionService.create(session);

        // Assert : on vérifie que la session a été créée correctement
        assertNotNull(result);
        assertEquals(session.getName(), result.getName());
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_session_success() {
        // Arrange : on configure le mock pour ne rien faire lors de la suppression
        doNothing().when(sessionRepository).deleteById(anyLong());

        // Act : on appelle la méthode de suppression
        sessionService.delete(1L);

        // Assert : on vérifie que la méthode de suppression a été appelée
        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_sessions_success() {
        // Arrange : on prépare une liste de sessions à retourner
        List<Session> sessions = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act : on récupère toutes les sessions
        List<Session> result = sessionService.findAll();

        // Assert : on vérifie que la liste contient bien la session attendue
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(session.getName(), result.get(0).getName());
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_session_exists() {
        // Arrange : on configure le mock pour retourner une session existante
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act : on récupère la session par son ID
        Session result = sessionService.getById(1L);

        // Assert : on vérifie que la session retournée correspond à celle attendue
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        verify(sessionRepository).findById(1L);
    }

    @Test
    void getById_session_not_found() {
        // Arrange : on configure le mock pour retourner un Optional vide (session inexistante)
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act : on tente de récupérer une session inexistante
        Session result = sessionService.getById(1L);

        // Assert : on vérifie que null est retourné pour une session inexistante
        assertNull(result);
        verify(sessionRepository).findById(1L);
    }

    @Test
    void update_session_success() {
        // Arrange : on prépare une session mise à jour avec de nouvelles données
        Session updatedSession = Session.builder()
                .id(1L)
                .name("Updated Session")
                .date(new Date())
                .description("Updated Description")
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();

        // On configure le mock pour retourner la session mise à jour
        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession);

        // Act : on met à jour la session
        Session result = sessionService.update(1L, updatedSession);

        // Assert : on vérifie que la session a été mise à jour correctement
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Session", result.getName());
        verify(sessionRepository).save(updatedSession);
    }

    @Test
    void participate_session_and_user_exist_success() {
        // Arrange : on configure les mocks pour retourner session et utilisateur existants
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act : on fait participer l'utilisateur à la session
        sessionService.participate(1L, 1L);

        // Assert : on vérifie que l'utilisateur a bien été ajouté à la liste des participants
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_session_not_found_throws_exception() {
        // Arrange : on configure le mock pour simuler une session inexistante
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert : on vérifie qu'une exception est levée quand la session n'existe pas
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void participate_user_not_found_throws_exception() {
        // Arrange : on configure les mocks (session existe, utilisateur n'existe pas)
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert : on vérifie qu'une exception est levée quand l'utilisateur n'existe pas
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void participate_user_already_participates_throws_exception() {
        // Arrange : on ajoute l'utilisateur à la session (déjà participant)
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert : on vérifie qu'une exception est levée si l'utilisateur participe déjà
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void noLongerParticipate_session_and_user_exist_success() {
        // Arrange : on ajoute l'utilisateur à la session puis on configure les mocks
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act : on retire l'utilisateur de la session
        sessionService.noLongerParticipate(1L, 1L);

        // Assert : on vérifie que l'utilisateur a bien été retiré de la liste des participants
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_session_not_found_throws_exception() {
        // Arrange : on configure le mock pour simuler une session inexistante
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert : on vérifie qu'une exception est levée quand la session n'existe pas
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void noLongerParticipate_user_not_participating_throws_exception() {
        // Arrange : on configure le mock pour une session sans l'utilisateur (pas participant)
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act & Assert : on vérifie qu'une exception est levée si l'utilisateur ne participe pas
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, never()).save(any(Session.class));
    }
}
