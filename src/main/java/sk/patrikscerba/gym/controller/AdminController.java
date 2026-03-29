package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.auth.ResetPasswordRequest;
import sk.patrikscerba.gym.dto.auth.ResetPasswordResponse;
import sk.patrikscerba.gym.dto.auth.SecurityQuestionResponse;
import sk.patrikscerba.gym.dto.employee.EmployeeAccountResponse;
import sk.patrikscerba.gym.dto.employee.EmployeeCreateRequest;
import sk.patrikscerba.gym.service.auth.PasswordResetService;
import sk.patrikscerba.gym.service.employee.EmployeeAccountService;

/**
 * Controller pre administrátorské operácie.
 * Obsahuje endpointy na vytvorenie účtu zamestnanca a na reset hesla používateľov
 * prostredníctvom bezpečnostnej otázky a odpovede.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final EmployeeAccountService employeeAccountService;
    private final PasswordResetService passwordResetService;

    public AdminController(EmployeeAccountService employeeAccountService,
                           PasswordResetService passwordResetService) {
        this.employeeAccountService = employeeAccountService;
        this.passwordResetService = passwordResetService;
    }

    // Endpoint na vytvorenie účtu zamestnanca.
    @PostMapping("/employees")
    public ResponseEntity<EmployeeAccountResponse> createEmployee(@Valid
                                                                  @RequestBody EmployeeCreateRequest request) {

        EmployeeAccountResponse response = employeeAccountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint na získanie bezpečnostnej otázky používateľa podľa emailu.
    @GetMapping("/users/security-question")
    public ResponseEntity<SecurityQuestionResponse> getSecurityQuestion(@RequestParam String email) {

        SecurityQuestionResponse response = passwordResetService.getSecurityQuestionByEmail(email);
        return ResponseEntity.ok(response);
    }

    // Endpoint na reset hesla používateľa po overení bezpečnostnej odpovede.
    @PostMapping("/users/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        ResetPasswordResponse response = passwordResetService.resetPasswordBySecurityAnswer(request);
        return ResponseEntity.ok(response);
    }
}


