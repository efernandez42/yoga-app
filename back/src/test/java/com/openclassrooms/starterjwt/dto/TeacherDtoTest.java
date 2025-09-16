package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherDtoTest {

    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacherDto = new TeacherDto();
    }

    @Test
    void constructor_noArgs_shouldCreateInstance() {
        TeacherDto dto = new TeacherDto();
        assertNotNull(dto);
    }

    @Test
    void getId_shouldReturnId() {
        Long id = 1L;
        teacherDto.setId(id);
        assertEquals(id, teacherDto.getId());
    }

    @Test
    void setId_shouldSetId() {
        Long id = 1L;
        teacherDto.setId(id);
        assertEquals(id, teacherDto.getId());
    }

    @Test
    void getFirstName_shouldReturnFirstName() {
        String firstName = "Jane";
        teacherDto.setFirstName(firstName);
        assertEquals(firstName, teacherDto.getFirstName());
    }

    @Test
    void setFirstName_shouldSetFirstName() {
        String firstName = "Jane";
        teacherDto.setFirstName(firstName);
        assertEquals(firstName, teacherDto.getFirstName());
    }

    @Test
    void getLastName_shouldReturnLastName() {
        String lastName = "Smith";
        teacherDto.setLastName(lastName);
        assertEquals(lastName, teacherDto.getLastName());
    }

    @Test
    void setLastName_shouldSetLastName() {
        String lastName = "Smith";
        teacherDto.setLastName(lastName);
        assertEquals(lastName, teacherDto.getLastName());
    }

    @Test
    void getCreatedAt_shouldReturnCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        teacherDto.setCreatedAt(createdAt);
        assertEquals(createdAt, teacherDto.getCreatedAt());
    }

    @Test
    void setCreatedAt_shouldSetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        teacherDto.setCreatedAt(createdAt);
        assertEquals(createdAt, teacherDto.getCreatedAt());
    }

    @Test
    void getUpdatedAt_shouldReturnUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        teacherDto.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, teacherDto.getUpdatedAt());
    }

    @Test
    void setUpdatedAt_shouldSetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        teacherDto.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, teacherDto.getUpdatedAt());
    }
}
