package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.EmployeeResponseDto;
import de.mtala.hrmanager.model.Contract;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.model.EmploymentStatus;
import de.mtala.hrmanager.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private final Instant hireDate = Instant.parse("2026-01-10T00:00:00Z");
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;
    private Department department;
    private Employee employee;

    @BeforeEach
    void setUp() {
        department = new Department("Engineering", "ENG");
        department.setId(1L);
        employee = employee(1L, "Ada", "Lovelace", "ada@example.com", department, BigDecimal.valueOf(100000));
    }

    @Test
    void getAllEmployeesMapsEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeResponseDto> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEmployee(result.getFirst(), employee);
        verify(employeeRepository).findAll();
    }

    private Employee employee(
            Long id, String firstName, String lastName, String email,
            Department employeeDepartment, BigDecimal salary) {
        Employee result = new Employee();
        result.setId(id);
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setEmail(email);
        result.setHireDate(hireDate);
        result.setStatus(EmploymentStatus.ACTIVE);
        result.setDepartment(employeeDepartment);
        if (salary != null) {
            Contract contract = new Contract();
            contract.setSalary(salary);
            contract.setStartDate(hireDate);
            result.setContract(contract);
        }
        return result;
    }

    private void assertEmployee(EmployeeResponseDto result, Employee expected) {
        assertEquals(expected.getId(), result.id());
        assertEquals(expected.getFirstName(), result.firstName());
        assertEquals(expected.getLastName(), result.lastName());
        assertEquals(expected.getEmail(), result.email());
        assertEquals(expected.getHireDate(), result.hireDate());
        assertEquals(expected.getStatus(), result.status());
        assertEquals(expected.getDepartment().getName(), result.departmentName());
        assertEquals(expected.getContract().getSalary(), result.salary());
    }
}
