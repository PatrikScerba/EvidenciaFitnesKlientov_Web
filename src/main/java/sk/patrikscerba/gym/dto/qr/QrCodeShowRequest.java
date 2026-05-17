package sk.patrikscerba.gym.dto.qr;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO objekt používaný na overenie bezpečnostnej odpovede pri zobrazení QR kódu klienta.
 * Obsahuje email používateľa a odpoveď na bezpečnostnú otázku.
 */
public class QrCodeShowRequest {

    @NotBlank(message = "Email používateľa je povinný.")
    @Email(message = "Email nemá správny formát.")
    private String email;

    @NotBlank(message = "Bezpečnostná odpoveď je povinná.")
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
