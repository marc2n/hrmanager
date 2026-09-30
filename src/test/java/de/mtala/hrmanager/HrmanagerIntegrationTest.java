package de.mtala.hrmanager;

import de.mtala.hrmanager.repository.DepartmentRepository;
import de.mtala.hrmanager.repository.EmployeeRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HrmanagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void cleanDatabase() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void departmentLifecycleWorksThroughHttpApi() throws Exception {
        String departmentId = createDepartment();

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(departmentId))))
                .andExpect(jsonPath("$[0].name", is("Engineering")))
                .andExpect(jsonPath("$[0].code", is("ENG")))
                .andExpect(jsonPath("$[0].employeeCount", is(0)));

        mockMvc.perform(get("/api/departments/{id}", departmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Engineering")));

        mockMvc.perform(delete("/api/departments/{id}", departmentId))
                .andExpect(status().isNoContent());

        assertThrows(ServletException.class, () ->
                mockMvc.perform(get("/api/departments/{id}", departmentId)));
    }

    @Test
    void duplicateDepartmentReturnsServerError() throws Exception {
        createDepartment();

        assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Engineering","code":"ENG"}
                                """)));
    }

    @Test
    void employeeLifecycleWorksThroughHttpApi() throws Exception {
        createDepartment();

        String employeeId = createEmployee();

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(employeeId))))
                .andExpect(jsonPath("$[0].firstName", is("Ada")))
                .andExpect(jsonPath("$[0].lastName", is("Lovelace")))
                .andExpect(jsonPath("$[0].departmentName", is("Engineering")))
                .andExpect(jsonPath("$[0].salary", is(100000.0)));

        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("ada@example.com")));

        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isNoContent());

        assertThrows(ServletException.class, () ->
                mockMvc.perform(get("/api/employees/{id}", employeeId)));
    }

    @Test
    void creatingEmployeeWithUnknownDepartmentReturnsServerError() {
        assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Unknown"))));
    }

    @Test
    void departmentWithEmployeesCannotBeDeleted() throws Exception {
        String departmentId = createDepartment();
        createEmployee();

        assertThrows(ServletException.class, () ->
                mockMvc.perform(delete("/api/departments/{id}", departmentId)));
    }

    @Test
    void invalidEmployeeRequestReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"","lastName":"Lovelace","email":"not-an-email"}
                                """))
                .andExpect(status().isBadRequest());
    }

    private String createDepartment() throws Exception {
        String response = mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"%s","code":"%s"}
                                """.formatted("Engineering", "ENG")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Engineering")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\"id\":(\\d+).*", "$1");
    }

    private String createEmployee() throws Exception {
        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson("Engineering")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Ada")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\"id\":(\\d+).*", "$1");
    }

    private String employeeJson(String departmentName) {
        return """
                {
                  "firstName":"Ada",
                  "lastName":"Lovelace",
                  "email":"ada@example.com",
                  "hireDate":"2026-01-10T00:00:00Z",
                  "status":"ACTIVE",
                  "departmentName":"%s",
                  "salary":100000
                }
                """.formatted(departmentName);
    }
}
