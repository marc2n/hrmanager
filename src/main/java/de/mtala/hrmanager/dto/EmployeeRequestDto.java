package de.mtala.hrmanager.dto;

import de.mtala.hrmanager.model.EmploymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

public record EmployeeRequestDto(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,
        @NotNull(message = "Hire date is required")
        Instant hireDate,
        @NotNull(message = "Employment status is required")
        EmploymentStatus status,
        @NotNull(message = "Department name is required")
        String departmentName,
        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than zero")
        BigDecimal salary
) {
}
