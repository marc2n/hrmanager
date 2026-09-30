package de.mtala.hrmanager.repository;

import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.model.EmploymentStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    List<Employee> findByStatus(EmploymentStatus status);

    @Query("SELECT e FROM Employee e JOIN FETCH e.department d WHERE d.id = :deptId")
    List<Employee> findByDepartmentIdWithDepartment(@Param("deptId") Long deptId);

    // Find employees created after a specific timestamp
    List<Employee> findByCreatedAtAfter(Instant timestamp);

    // Find all employees and sort them by when they were last updated (newest first)
    List<Employee> findAllByOrderByUpdatedAtDesc();
}
