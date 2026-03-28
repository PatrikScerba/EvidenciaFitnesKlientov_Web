package sk.patrikscerba.gym.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sk.patrikscerba.gym.enums.SecurityQuestion;

/**
 * DTO pre vytvorenie účtu zamestnanca.
 */
public class EmployeeCreateRequest {

    @Email(message = "Zadajte platný email")
    @NotBlank(message = "Zadajte email")
    private String email;

    @NotNull(message = "Vyberte bezpečnostnú otázku.")
    private SecurityQuestion securityQuestion;

    @NotBlank(message = "Zadajte bezpečnostnú odpoveď.")
    @Size(min = 3, max = 100, message = "Bezpečnostná odpoveď musí mať 3 až 100 znakov.")
    private String securityAnswer;

    @NotBlank(message = "Potvrďte bezpečnostnú odpoveď.")
    @Size(min = 3, max = 100, message = "Potvrdenie bezpečnostnej odpovede musí mať 3 až 100 znakov.")
    private String confirmSecurityAnswer;

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getConfirmSecurityAnswer() {
        return confirmSecurityAnswer;
    }

    public void setConfirmSecurityAnswer(String confirmSecurityAnswer) {
        this.confirmSecurityAnswer = confirmSecurityAnswer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

