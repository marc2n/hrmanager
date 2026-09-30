package de.mtala.hrmanager.service;

import de.mtala.hrmanager.dto.DepartmentRequestDto;
import de.mtala.hrmanager.dto.DepartmentResponseDto;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));

        log.info("department with ID: {} was successfully retrieved", id);
        return mapToDto(department);
    }

    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto) {
        if (departmentRepository.findByName(requestDto.name()).isPresent()) {
            throw new IllegalArgumentException("Department already exists with name: " + requestDto.name());
        }

        Department department = new Department();
        department.setName(requestDto.name());
        department.setCode(requestDto.code());

        Department savedDepartment = departmentRepository.save(department);
        log.info("department with ID: {} was created", savedDepartment.getId());
        return mapToDto(savedDepartment);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));

        // Optional safety check: Prevent deletion if employees are still attached
        if (!department.getEmployees().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with active employees. Reassign or remove employees first.");
        }

        departmentRepository.delete(department);
        log.info("department with ID: {} was deleted", id);
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
