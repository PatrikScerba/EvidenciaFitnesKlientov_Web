package sk.patrikscerba.gym.dto.auth;

/**
 * DTO odpoveď po úspešnom resete hesla.
 * Obsahuje email používateľa, nové dočasné heslo a informačnú správu.
 */
public class ResetPasswordResponse {

    private String email;
    private String temporaryPassword;
    private String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(String temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

