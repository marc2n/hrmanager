package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.EmployeeRequestDto;
import de.mtala.hrmanager.dto.EmployeeResponseDto;
import de.mtala.hrmanager.model.Contract;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.model.EmploymentStatus;
import de.mtala.hrmanager.repository.DepartmentRepository;
import de.mtala.hrmanager.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private final Instant hireDate = Instant.parse("2026-01-10T00:00:00Z");
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;
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

    @Test
    void getEmployeeByIdReturnsMappedEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponseDto result = employeeService.getEmployeeById(1L);

        assertEmployee(result, employee);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeByIdThrowsWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(99L)
        );

        assertEquals("Employee not found with ID: 99", exception.getMessage());
        verify(employeeRepository).findById(99L);
    }

    @Test
    void createEmployeeSavesEmployeeAndContract() {
        EmployeeRequestDto request = getNewEmployeeRequestDto();
        when(employeeRepository.findByEmail("ada@example.com")).thenReturn(Optional.empty());
        when(departmentRepository.findByName("Engineering")).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponseDto result = employeeService.createEmployee(request);

        assertEmployee(result, employee);
        verify(employeeRepository).findByEmail("ada@example.com");
        verify(departmentRepository).findByName("Engineering");
        verify(employeeRepository).save(org.mockito.ArgumentMatchers.argThat(saved ->
                "Ada".equals(saved.getFirstName())
                        && "Lovelace".equals(saved.getLastName())
                        && saved.getDepartment() == department
                        && saved.getContract() != null
                        && (BigDecimal.valueOf(100000).compareTo(saved.getContract().getSalary())) == 0
                        && hireDate.equals(saved.getContract().getStartDate())));
    }

    @Test
    void createEmployeeThrowsWhenEmailIsAlreadyInUse() {
        EmployeeRequestDto request = getNewEmployeeRequestDto();
        when(employeeRepository.findByEmail("ada@example.com")).thenReturn(Optional.of(employee));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.createEmployee(request)
        );

        assertEquals("Email is already in use: ada@example.com", exception.getMessage());
        verify(employeeRepository).findByEmail("ada@example.com");
        verifyNoMoreInteractions(employeeRepository, departmentRepository);
    }

    @Test
    void createEmployeeThrowsWhenDepartmentDoesNotExist() {
        EmployeeRequestDto request = getNewEmployeeRequestDto();
        when(employeeRepository.findByEmail("ada@example.com")).thenReturn(Optional.empty());
        when(departmentRepository.findByName("Engineering")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.createEmployee(request)
        );

        assertEquals("Department not found with name: Engineering", exception.getMessage());
        verify(employeeRepository).findByEmail("ada@example.com");
        verify(departmentRepository).findByName("Engineering");
        verifyNoMoreInteractions(employeeRepository, departmentRepository);
    }

    @Test
    void deleteEmployeeDeletesExistingEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployeeThrowsWhenEmployeeDoesNotExist() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.deleteEmployee(99L)
        );

        assertEquals("Employee not found with ID: 99", exception.getMessage());
        verify(employeeRepository).existsById(99L);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void getEmployeesJoinedInLastWeekQueriesAndMapsEmployees() {
        when(employeeRepository.findByCreatedAtAfter(any(Instant.class))).thenReturn(List.of(employee));

        List<EmployeeResponseDto> result = employeeService.getEmployeesJoinedInLastWeek();

        assertEquals(1, result.size());
        assertEmployee(result.getFirst(), employee);
        verify(employeeRepository).findByCreatedAtAfter(org.mockito.ArgumentMatchers.argThat(timestamp ->
                timestamp.isAfter(Instant.now().minus(7, ChronoUnit.DAYS).minusSeconds(2))
                        && timestamp.isBefore(Instant.now())));
    }

    @Test
    void mappingAllowsEmployeeWithoutOptionalRelations() {
        Employee withoutRelations = employee(2L, "Grace", "Hopper", "grace@example.com", null, null);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(withoutRelations));

        EmployeeResponseDto result = employeeService.getEmployeeById(2L);

        assertEquals("Grace", result.firstName());
        assertNull(result.departmentName());
        assertNull(result.salary());
    }

    private EmployeeRequestDto getNewEmployeeRequestDto() {
        return new EmployeeRequestDto(
                "Ada",
                "Lovelace",
                "ada@example.com",
                hireDate,
                EmploymentStatus.ACTIVE,
                "Engineering",
                BigDecimal.valueOf(100000)
        );
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
