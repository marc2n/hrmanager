package de.mtala.hrmanager.controller;

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
}
