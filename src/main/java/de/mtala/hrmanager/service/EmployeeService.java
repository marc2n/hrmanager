package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.EmployeeRequestDto;
import de.mtala.hrmanager.dto.EmployeeResponseDto;
import de.mtala.hrmanager.model.Contract;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.repository.DepartmentRepository;
import de.mtala.hrmanager.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        List<EmployeeResponseDto> employeeResponseDtoList = employeeRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();

        log.info("all employees retrieved");

        return employeeResponseDtoList;
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
        log.info("employee with ID: {} was successfully retrieved", id);
        return mapToDto(employee);
    }

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        if (employeeRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use: " + requestDto.email());
        }

        Department department = departmentRepository.findByName(requestDto.departmentName())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + requestDto.departmentName()));

        Employee employee = new Employee();
        employee.setFirstName(requestDto.firstName());
        employee.setLastName(requestDto.lastName());
        employee.setEmail(requestDto.email());
        employee.setHireDate(requestDto.hireDate());
        employee.setStatus(requestDto.status());
        employee.setDepartment(department);

        // Create and link contract
        Contract contract = new Contract();
        contract.setSalary(requestDto.salary());
        contract.setStartDate(requestDto.hireDate());
        employee.setContract(contract);

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Created new employee with ID: {}", savedEmployee.getId());
        return mapToDto(savedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("employee with ID: {} was deleted", id);
    }

    public List<EmployeeResponseDto> getEmployeesJoinedInLastWeek() {
        Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);

        // Call the derived query method from the repository
        List<Employee> recentEmployees = employeeRepository.findByCreatedAtAfter(oneWeekAgo);

        return recentEmployees.stream()
                .map(this::mapToDto)
                .toList();
    }

    private EmployeeResponseDto mapToDto(Employee employee) {
        BigDecimal salary = (employee.getContract() != null) ? employee.getContract().getSalary() : null;
        String deptName = (employee.getDepartment() != null) ? employee.getDepartment().getName() : null;

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getHireDate(),
                employee.getStatus(),
                deptName,
                salary
        );
    }
}
