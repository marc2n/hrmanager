package de.mtala.hrmanager.dto;

public record DepartmentResponseDto(
        Long id,
        String name,
        String code,
        int employeeCount
) {
}
