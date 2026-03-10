package sk.patrikscerba.gym.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * *DTO objekt používaný pri vytváraní nového klienta.
 */
public class ClientCreateRequest {

    @NotBlank(message = "Pole Krstné meno nesmie byť prázdne")
    @Size(min = 2, max = 50, message = "Meno musí mať 2 až 50 znakov.")
    private String firstName;

    @NotBlank(message = "Pole priezvisko nesmie byť prázdne")
    @Size(min = 2, max = 50, message = "Meno musí mať 2 až 50 znakov.")
    private String lastName;

    @NotNull(message = "Pole dátum narodenia nesmie byť prázdne")
    @Past(message = "Zadajte platný dátum narodenia.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Pole telefónne číslo nesmie byť prázdne")
    private String phoneNumber;

    @NotBlank(message = "Pole adresa nesmie byť prázdne")
    @Size(max = 100, message = "Adresa môže mať maximálne 100 znakov.")
    private String address;

    @Email(message = "Neplatný formát emailu")
    @NotBlank(message = "Pole email nesmie byť prázdne")
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
