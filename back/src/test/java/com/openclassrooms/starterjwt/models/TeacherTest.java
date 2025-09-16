package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeacherTest {

    private Teacher teacher;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        now = LocalDateTime.now();
    }

    @Test
    void constructor_withNoArgs_shouldCreateInstance() {
        Teacher newTeacher = new Teacher();
        assertNotNull(newTeacher);
    }

    @Test
    void constructor_withAllArgs_shouldCreateInstance() {
        Teacher newTeacher = new Teacher(1L, "Doe", "John", now, now);
        assertNotNull(newTeacher);
        assertEquals(1L, newTeacher.getId());
        assertEquals("John", newTeacher.getFirstName());
        assertEquals("Doe", newTeacher.getLastName());
        assertEquals(now, newTeacher.getCreatedAt());
        assertEquals(now, newTeacher.getUpdatedAt());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        teacher.setId(id);
        assertEquals(id, teacher.getId());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        String firstName = "John";
        teacher.setFirstName(firstName);
        assertEquals(firstName, teacher.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        String lastName = "Doe";
        teacher.setLastName(lastName);
        assertEquals(lastName, teacher.getLastName());
    }

    @Test
    void setCreatedAt_shouldSetCreatedAt() {
        teacher.setCreatedAt(now);
        assertEquals(now, teacher.getCreatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetUpdatedAt() {
        teacher.setUpdatedAt(now);
        assertEquals(now, teacher.getUpdatedAt());
    }

    // Note: Teacher model doesn't have sessions relationship in this implementation

    @Test
    void equals_withSameId_shouldReturnTrue() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        
        assertEquals(teacher1, teacher2);
    }

    @Test
    void equals_withDifferentId_shouldReturnFalse() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        
        assertNotEquals(teacher1, teacher2);
    }

    @Test
    void equals_withNull_shouldReturnFalse() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        
        assertNotEquals(teacher1, null);
    }

    @Test
    void equals_withSameObject_shouldReturnTrue() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        
        assertEquals(teacher1, teacher1);
    }

    @Test
    void hashCode_withSameId_shouldReturnSameHashCode() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void toString_shouldReturnStringRepresentation() {
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        
        String result = teacher.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}
