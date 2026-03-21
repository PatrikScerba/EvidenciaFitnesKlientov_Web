package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.employee.EmployeeAccountResponse;
import sk.patrikscerba.gym.dto.employee.EmployeeCreateRequest;
import sk.patrikscerba.gym.service.employee.EmployeeAccountService;

/**
 * Controller pre administrátorské operácie.
 * Táto trieda spracováva endpointy, ku ktorým má mať prístup admin.
 * Aktuálne slúži na vytvorenie nového účtu zamestnanca.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final EmployeeAccountService employeeAccountService;

    public AdminController(EmployeeAccountService employeeAccountService) {
        this.employeeAccountService = employeeAccountService;
    }

    // Endpoint na vytvorenie účtu zamestnanca.
    @PostMapping("/employees")
    public ResponseEntity<EmployeeAccountResponse> createEmployee(@Valid
            @RequestBody EmployeeCreateRequest request) {

        EmployeeAccountResponse response = employeeAccountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


