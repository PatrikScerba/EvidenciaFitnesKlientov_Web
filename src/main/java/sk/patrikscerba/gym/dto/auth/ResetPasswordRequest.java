package sk.patrikscerba.gym.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pre požiadavku na reset hesla pomocou emailu a bezpečnostnej odpovede.
 */
public class ResetPasswordRequest {

    @NotBlank(message = "Zadajte email používateľa.")
    @Email(message = "Zadajte platný email.")
    private String email;

    @NotBlank(message = "Zadajte bezpečnostnú odpoveď.")
    private String securityAnswer;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}


