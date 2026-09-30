package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.DepartmentResponseDto;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.repository.DepartmentRepository;
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

    private void assertDepartment(
            DepartmentResponseDto result, Long id, String name, String code, int employeeCount) {
        assertEquals(id, result.id());
        assertEquals(name, result.name());
        assertEquals(code, result.code());
        assertEquals(employeeCount, result.employeeCount());
    }
}
