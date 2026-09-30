package de.mtala.hrmanager.config;

import de.mtala.hrmanager.model.Contract;
import de.mtala.hrmanager.model.Department;
import de.mtala.hrmanager.model.Employee;
import de.mtala.hrmanager.model.EmploymentStatus;
import de.mtala.hrmanager.repository.DepartmentRepository;
import de.mtala.hrmanager.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@ConditionalOnProperty(name = "app.init-db", havingValue = "true")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        return args -> {
            // Prevent duplicate seeding if data already exists
            if (departmentRepository.count() > 0) {
                return;
            }

            // Create Departments
            Department engineering = new Department("Engineering", "ENG");
            Department hr = new Department("Human Resources", "HR");
            Department finance = new Department("Finance", "FIN");

            departmentRepository.saveAll(List.of(engineering, hr, finance));

            // Create Employees & Contracts
            Employee alice = new Employee();
            alice.setFirstName("Alice");
            alice.setLastName("Smith");
            alice.setEmail("alice.smith@example.com");
            alice.setHireDate(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneOffset.UTC).toInstant());
            alice.setStatus(EmploymentStatus.ACTIVE);
            alice.setDepartment(engineering);

            Contract contract1 = new Contract();
            contract1.setSalary(new BigDecimal("78000.00"));
            contract1.setStartDate(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneOffset.UTC).toInstant());
            alice.setContract(contract1);

            Employee bob = new Employee();
            bob.setFirstName("Bob");
            bob.setLastName("Johnson");
            bob.setEmail("bob.johnson@example.com");
            bob.setHireDate(LocalDate.of(2022, 5, 10).atStartOfDay(ZoneOffset.UTC).toInstant());
            bob.setStatus(EmploymentStatus.ACTIVE);
            bob.setDepartment(hr);

            Contract contract2 = new Contract();
            contract2.setSalary(new BigDecimal("65000.00"));
            contract2.setStartDate(LocalDate.of(2022, 5, 10).atStartOfDay(ZoneOffset.UTC).toInstant());
            bob.setContract(contract2);

            Employee charlie = new Employee();
            charlie.setFirstName("Charlie");
            charlie.setLastName("Brown");
            charlie.setEmail("charlie.brown@example.com");
            charlie.setHireDate(LocalDate.of(2024, 3, 1).atStartOfDay(ZoneOffset.UTC).toInstant());
            charlie.setStatus(EmploymentStatus.PROBATION);
            charlie.setDepartment(engineering);

            Contract contract3 = new Contract();
            contract3.setSalary(new BigDecimal("82000.00"));
            contract3.setStartDate(LocalDate.of(2024, 3, 1).atStartOfDay(ZoneOffset.UTC).toInstant());
            charlie.setContract(contract3);

            employeeRepository.saveAll(List.of(alice, bob, charlie));
        };
    }
}
