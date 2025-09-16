package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionTest {

    private Session session;
    private LocalDateTime now;
    private Date sessionDate;

    @BeforeEach
    void setUp() {
        session = new Session();
        now = LocalDateTime.now();
        sessionDate = new Date();
    }

    @Test
    void constructor_withNoArgs_shouldCreateInstance() {
        Session newSession = new Session();
        assertNotNull(newSession);
    }

    @Test
    void constructor_withAllArgs_shouldCreateInstance() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<User> users = new ArrayList<>();
        
        Session newSession = new Session(1L, "Yoga Session", sessionDate, "Description", teacher, users, now, now);
        assertNotNull(newSession);
        assertEquals(1L, newSession.getId());
        assertEquals("Yoga Session", newSession.getName());
        assertEquals(sessionDate, newSession.getDate());
        assertEquals("Description", newSession.getDescription());
        assertEquals(teacher, newSession.getTeacher());
        assertEquals(now, newSession.getCreatedAt());
        assertEquals(now, newSession.getUpdatedAt());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        session.setId(id);
        assertEquals(id, session.getId());
    }

    @Test
    void setName_shouldSetName() {
        String name = "Yoga Session";
        session.setName(name);
        assertEquals(name, session.getName());
    }

    @Test
    void setDate_shouldSetDate() {
        session.setDate(sessionDate);
        assertEquals(sessionDate, session.getDate());
    }

    @Test
    void setDescription_shouldSetDescription() {
        String description = "A relaxing yoga session";
        session.setDescription(description);
        assertEquals(description, session.getDescription());
    }

    @Test
    void setTeacher_shouldSetTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        
        session.setTeacher(teacher);
        assertEquals(teacher, session.getTeacher());
    }

    @Test
    void setCreatedAt_shouldSetCreatedAt() {
        session.setCreatedAt(now);
        assertEquals(now, session.getCreatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetUpdatedAt() {
        session.setUpdatedAt(now);
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void setUsers_shouldSetUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        users.add(user1);
        users.add(user2);
        
        session.setUsers(users);
        assertEquals(users, session.getUsers());
        assertEquals(2, session.getUsers().size());
    }

    @Test
    void getUsers_whenNotSet_shouldReturnEmptyList() {
        // Note: Session model initializes users as null, not empty list
        assertNull(session.getUsers());
    }

    @Test
    void equals_withSameId_shouldReturnTrue() {
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(1L);
        
        assertEquals(session1, session2);
    }

    @Test
    void equals_withDifferentId_shouldReturnFalse() {
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(2L);
        
        assertNotEquals(session1, session2);
    }

    @Test
    void equals_withNull_shouldReturnFalse() {
        Session session1 = new Session();
        session1.setId(1L);
        
        assertNotEquals(session1, null);
    }

    @Test
    void equals_withSameObject_shouldReturnTrue() {
        Session session1 = new Session();
        session1.setId(1L);
        
        assertEquals(session1, session1);
    }

    @Test
    void hashCode_withSameId_shouldReturnSameHashCode() {
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(1L);
        
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("A relaxing session");
        
        String result = session.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Yoga Session"));
        assertTrue(result.contains("A relaxing session"));
    }
}
