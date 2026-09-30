package de.mtala.hrmanager.controller;

import de.mtala.hrmanager.dto.EmployeeResponseDto;
import de.mtala.hrmanager.model.EmploymentStatus;
import de.mtala.hrmanager.service.EmployeeService;
import java.math.BigDecimal;
import java.time.Instant;
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
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployeesReturnsOkAndEmployees() {
        List<EmployeeResponseDto> employees = List.of(employee());
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<EmployeeResponseDto>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(employees, response.getBody());
        verify(employeeService).getAllEmployees();
    }

    private EmployeeResponseDto employee() {
        return new EmployeeResponseDto(
                1L,
                "Ada",
                "Lovelace",
                "ada@example.com",
                Instant.parse("2026-01-10T00:00:00Z"),
                EmploymentStatus.ACTIVE,
                "Engineering",
                BigDecimal.valueOf(100000)
        );
    }
}
