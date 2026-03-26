package sk.patrikscerba.gym.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO pre zmenu hesla prihláseného používateľa.
 * Obsahuje pôvodné heslo, nové heslo a potvrdenie nového hesla.
 */
public class ChangePasswordRequest {

    @NotBlank(message = "Zadajte pôvodné heslo.")
    private String oldPassword;

    @NotBlank(message = "Zadajte nové heslo.")
    private String newPassword;

    @NotBlank(message = "Potvrďte nové heslo.")
    private  String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
