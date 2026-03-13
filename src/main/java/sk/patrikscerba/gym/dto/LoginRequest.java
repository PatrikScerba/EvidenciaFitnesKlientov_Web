package sk.patrikscerba.gym.dto;

/**
 * DTO objekt pre prihlásenie používateľa.
 * Obsahuje údaje, ktoré používateľ zadáva pri login requeste.
 */
public class LoginRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
