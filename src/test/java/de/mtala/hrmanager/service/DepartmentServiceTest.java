package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.DepartmentRequestDto;
import de.mtala.hrmanager.dto.DepartmentResponseDto;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("Engineering", "ENG");
        department.setId(1L);
    }

    @Test
    void getAllDepartmentsMapsDepartmentsAndEmployeeCounts() {
        Department secondDepartment = new Department("HR", "HR");
        secondDepartment.setId(2L);
        department.addEmployee(new Employee());

        when(departmentRepository.findAll()).thenReturn(List.of(department, secondDepartment));

        List<DepartmentResponseDto> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        assertDepartment(result.get(0), 1L, "Engineering", "ENG", 1);
        assertDepartment(result.get(1), 2L, "HR", "HR", 0);
        verify(departmentRepository).findAll();
    }

    @Test
    void getDepartmentByIdReturnsMappedDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentResponseDto result = departmentService.getDepartmentById(1L);

        assertDepartment(result, 1L, "Engineering", "ENG", 0);
        verify(departmentRepository).findById(1L);
    }

    @Test
    void getDepartmentByIdThrowsWhenDepartmentDoesNotExist() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.getDepartmentById(99L)
        );

        assertEquals("Department not found with ID: 99", exception.getMessage());
        verify(departmentRepository).findById(99L);
    }

    @Test
    void createDepartmentSavesAndReturnsDepartment() {
        DepartmentRequestDto request = new DepartmentRequestDto("Engineering", "ENG");
        when(departmentRepository.findByName("Engineering")).thenReturn(Optional.empty());
        when(departmentRepository.save(org.mockito.ArgumentMatchers.any(Department.class))).thenReturn(department);

        DepartmentResponseDto result = departmentService.createDepartment(request);

        assertDepartment(result, 1L, "Engineering", "ENG", 0);
        verify(departmentRepository).findByName("Engineering");
        verify(departmentRepository).save(org.mockito.ArgumentMatchers.argThat(saved ->
                "Engineering".equals(saved.getName()) && "ENG".equals(saved.getCode())));
    }

    @Test
    void createDepartmentThrowsWhenNameAlreadyExists() {
        DepartmentRequestDto request = new DepartmentRequestDto("Engineering", null);
        when(departmentRepository.findByName("Engineering")).thenReturn(Optional.of(department));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> departmentService.createDepartment(request)
        );

        assertEquals("Department already exists with name: Engineering", exception.getMessage());
        verify(departmentRepository).findByName("Engineering");
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void deleteDepartmentDeletesEmptyDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).findById(1L);
        verify(departmentRepository).delete(department);
    }

    @Test
    void deleteDepartmentThrowsWhenDepartmentDoesNotExist() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> departmentService.deleteDepartment(99L)
        );

        assertEquals("Department not found with ID: 99", exception.getMessage());
        verify(departmentRepository).findById(99L);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void deleteDepartmentThrowsWhenEmployeesAreAttached() {
        department.addEmployee(new Employee());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> departmentService.deleteDepartment(1L)
        );

        assertEquals(
                "Cannot delete department with active employees. Reassign or remove employees first.",
                exception.getMessage()
        );
        verify(departmentRepository).findById(1L);
        verifyNoMoreInteractions(departmentRepository);
    }

    private void assertDepartment(
            DepartmentResponseDto result, Long id, String name, String code, int employeeCount) {
        assertEquals(id, result.id());
        assertEquals(name, result.name());
        assertEquals(code, result.code());
        assertEquals(employeeCount, result.employeeCount());
    }
}
