package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionDtoTest {

    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        sessionDto = new SessionDto();
    }

    @Test
    void constructor_noArgs_shouldCreateInstance() {
        SessionDto dto = new SessionDto();
        assertNotNull(dto);
    }

    @Test
    void getId_shouldReturnId() {
        Long id = 1L;
        sessionDto.setId(id);
        assertEquals(id, sessionDto.getId());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        sessionDto.setId(id);
        assertEquals(id, sessionDto.getId());
    }

    @Test
    void getName_shouldReturnName() {
        String name = "Yoga Session";
        sessionDto.setName(name);
        assertEquals(name, sessionDto.getName());
    }

    @Test
    void setName_shouldSetName() {
        String name = "Yoga Session";
        sessionDto.setName(name);
        assertEquals(name, sessionDto.getName());
    }

    @Test
    void getDate_shouldReturnDate() {
        java.util.Date date = new java.util.Date();
        sessionDto.setDate(date);
        assertEquals(date, sessionDto.getDate());
    }

    @Test
    void setDate_shouldSetDate() {
        java.util.Date date = new java.util.Date();
        sessionDto.setDate(date);
        assertEquals(date, sessionDto.getDate());
    }

    @Test
    void getDescription_shouldReturnDescription() {
        String description = "A relaxing yoga session";
        sessionDto.setDescription(description);
        assertEquals(description, sessionDto.getDescription());
    }

    @Test
    void setDescription_shouldSetDescription() {
        String description = "A relaxing yoga session";
        sessionDto.setDescription(description);
        assertEquals(description, sessionDto.getDescription());
    }

    @Test
    void getCreatedAt_shouldReturnCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        sessionDto.setCreatedAt(createdAt);
        assertEquals(createdAt, sessionDto.getCreatedAt());
    }

    @Test
    void setCreatedAt_shouldSetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        sessionDto.setCreatedAt(createdAt);
        assertEquals(createdAt, sessionDto.getCreatedAt());
    }

    @Test
    void getUpdatedAt_shouldReturnUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        sessionDto.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, sessionDto.getUpdatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        sessionDto.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, sessionDto.getUpdatedAt());
    }

    @Test
    void getTeacher_id_shouldReturnTeacherId() {
        Long teacherId = 1L;
        sessionDto.setTeacher_id(teacherId);
        assertEquals(teacherId, sessionDto.getTeacher_id());
    }

    @Test
    void setTeacher_id_shouldSetTeacherId() {
        Long teacherId = 1L;
        sessionDto.setTeacher_id(teacherId);
        assertEquals(teacherId, sessionDto.getTeacher_id());
    }
}
