package de.mtala.hrmanager.repository;

import de.mtala.hrmanager.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
