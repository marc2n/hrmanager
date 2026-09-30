package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.EmployeeResponseDto;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.repository.EmployeeRepository;
import java.math.BigDecimal;
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

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        List<EmployeeResponseDto> employeeResponseDtoList = employeeRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();

        log.info("all employees retrieved");

        return employeeResponseDtoList;
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
