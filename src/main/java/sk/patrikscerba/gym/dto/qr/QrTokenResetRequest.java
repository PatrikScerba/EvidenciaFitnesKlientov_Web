package sk.patrikscerba.gym.dto.qr;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pre administrátorský reset QR tokenu klienta.
 * Obsahuje email používateľa a bezpečnostnú odpoveď,
 * podľa ktorých sa overí oprávnenie na vygenerovanie nového QR tokenu.
 */
public class QrTokenResetRequest {

    @NotBlank(message = "Email používateľa je povinný.")
    @Email(message = "Email musí mať platný formát.")
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
