package de.mtala.hrmanager.repository;

import de.mtala.hrmanager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
