package sk.patrikscerba.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
  DTO objekt používaný pri úprave existujúceho klienta.
  Obsahuje údaje, ktoré prichádzajú v requeste pri aktualizácii klienta.
 */
public class ClientUpdateRequest {

    @NotBlank(message = "Krstné meno je povinné.")
    private String firstName;

    @NotBlank(message = "Priezvisko je povinné.")
    private String lastName;

    @NotNull(message = "Dátum narodenia je povinný.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Telefónne číslo je povinné.")
    private String phoneNumber;

    @NotBlank(message = "Adresa je povinná.")
    private String address;

    @NotBlank(message = "Email je povinný.")
    @Email(message = "Email nemá správny formát.")
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
