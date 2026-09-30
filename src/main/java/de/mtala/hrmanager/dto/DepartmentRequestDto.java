package de.mtala.hrmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDto(
        @NotBlank(message = "Department name is required")
        @Size(max = 100, message = "Department name must not exceed 100 characters")
        String name,
        @Size(max = 50, message = "Department code must not exceed 50 characters")
        String code
) {
}
