package sk.patrikscerba.gym.dto.auth;

/**
 * DTO objekt vracaný po úspešnom prihlásení používateľa.
 * Obsahuje základné informácie o prihlásenom používateľovi.
 */
public class LoginResponse {

    private long userId;
    private String email;
    private String role;
    private Long clientId;
    private String message;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
