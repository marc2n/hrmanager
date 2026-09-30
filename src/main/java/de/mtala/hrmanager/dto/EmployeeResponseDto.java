package de.mtala.hrmanager.dto;

import de.mtala.hrmanager.model.EmploymentStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record EmployeeResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Instant hireDate,
        EmploymentStatus status,
        String departmentName,
        BigDecimal salary
) {
}
