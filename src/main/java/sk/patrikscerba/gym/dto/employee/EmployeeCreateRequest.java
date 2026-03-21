package sk.patrikscerba.gym.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pre vytvorenie účtu zamestnanca.
 */
public class EmployeeCreateRequest {

    @Email(message = "Zadajte platný email")
    @NotBlank(message = "Zadajte email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

