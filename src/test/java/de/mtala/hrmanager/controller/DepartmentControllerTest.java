package de.mtala.hrmanager.controller;

import de.mtala.hrmanager.dto.DepartmentRequestDto;
import de.mtala.hrmanager.dto.DepartmentResponseDto;
import de.mtala.hrmanager.service.DepartmentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @Test
    void getAllDepartmentsReturnsOkAndDepartments() {
        List<DepartmentResponseDto> departments = List.of(new DepartmentResponseDto(1L, "Engineering", "ENG", 2));
        when(departmentService.getAllDepartments()).thenReturn(departments);

        ResponseEntity<List<DepartmentResponseDto>> response = departmentController.getAllDepartments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(departments, response.getBody());
        verify(departmentService).getAllDepartments();
    }

    @Test
    void getDepartmentByIdReturnsOkAndDepartment() {
        DepartmentResponseDto department = new DepartmentResponseDto(1L, "Engineering", "ENG", 2);
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        ResponseEntity<DepartmentResponseDto> response = departmentController.getDepartmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(department, response.getBody());
        verify(departmentService).getDepartmentById(1L);
    }

    @Test
    void createDepartmentReturnsCreatedAndDepartment() {
        DepartmentRequestDto request = new DepartmentRequestDto("Engineering", "ENG");
        DepartmentResponseDto created = new DepartmentResponseDto(1L, "Engineering", "ENG", 0);
        when(departmentService.createDepartment(request)).thenReturn(created);

        ResponseEntity<DepartmentResponseDto> response = departmentController.createDepartment(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(created, response.getBody());
        verify(departmentService).createDepartment(request);
    }

    @Test
    void deleteDepartmentReturnsNoContent() {
        ResponseEntity<Void> response = departmentController.deleteDepartment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(departmentService).deleteDepartment(1L);
    }
}
