package sk.patrikscerba.gym.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO trieda, ktorá predstavuje požiadavku na odoslanie emailu.
 * Obsahuje emailovú adresu príjemcu, meno príjemcu,
 * predmet a voliteľný obsah správy.
 */
public class EmailRequest {

    @NotBlank(message = "Email príjemcu nesmie byť prázdny.")
    @Email(message = "Email príjemcu musí mať platný formát.")
    private String to;

    @NotBlank(message = "Predmet emailu nesmie byť prázdny.")
    @Size(max = 150, message = "Predmet emailu môže mať maximálne 150 znakov.")
    private String subject;

    @Size(max = 5000, message = "Správa emailu môže mať maximálne 5000 znakov.")
    private String message;

    private String recipientName;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


