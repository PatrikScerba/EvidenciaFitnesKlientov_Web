package sk.patrikscerba.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * DTO trieda pre registráciu používateľa.
 * Slúži na prijatie údajov z registračného formulára.
 */
public class UserRegisterRequest {

    @Email(message = "Zadajte platný email")
    @NotBlank(message = "Zadajte používateľský email")
    private String email;

    @NotBlank(message = "Zadajte heslo")
    @Size(min=8, message ="Heslo musí obsahovať aspoň 8 znakov" )
    private String password;

    @NotBlank(message = "Zadajte znova heslo")
    @Size(min=8, message ="Heslo musí obsahovať aspoň 8 znakov" )
    private  String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
