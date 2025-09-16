package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        teacher2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build();
    }

    @Test
    void findAll_teachers_success() {
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(teacher1.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacher2.getFirstName(), result.get(1).getFirstName());
        verify(teacherRepository).findAll();
    }

    @Test
    void findAll_teachers_empty_list() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList());

        List<Teacher> result = teacherService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(teacherRepository).findAll();
    }

    @Test
    void findById_teacher_exists() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        Teacher result = teacherService.findById(1L);

        assertNotNull(result);
        assertEquals(teacher1.getId(), result.getId());
        assertEquals(teacher1.getFirstName(), result.getFirstName());
        assertEquals(teacher1.getLastName(), result.getLastName());
        verify(teacherRepository).findById(1L);
    }

    @Test
    void findById_teacher_not_found() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(1L);

        assertNull(result);
        verify(teacherRepository).findById(1L);
    }

    @Test
    void findById_null_id() {
        Teacher result = teacherService.findById(null);

        assertNull(result);
        verify(teacherRepository, never()).findById(anyLong());
    }

    @Test
    void findById_negative_id() {
        Teacher result = teacherService.findById(-1L);

        assertNull(result);
        verify(teacherRepository).findById(-1L);
    }
}
