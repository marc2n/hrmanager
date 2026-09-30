package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.DepartmentResponseDto;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.repository.DepartmentRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> getAllDepartments() {
        List<DepartmentResponseDto> departmentResponseDtoList = departmentRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();

        log.info("all employees retrieved");

        return departmentResponseDtoList;
    }

    private DepartmentResponseDto mapToDto(Department department) {
        int empCount = (department.getEmployees() != null) ? department.getEmployees().size() : 0;
        return new DepartmentResponseDto(
                department.getId(),
                department.getName(),
                department.getCode(),
                empCount
        );
    }
}
