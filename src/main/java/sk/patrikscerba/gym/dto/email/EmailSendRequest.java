package sk.patrikscerba.gym.dto.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO predstavujúce požiadavku na odoslanie emailu klientom.
 * Obsahuje predmet, voliteľný obsah správy,
 * voľbu odoslania všetkým klientom a zoznam vybraných klientov.
 */
public class EmailSendRequest {

    @NotBlank(message = "Predmet emailu nesmie byť prázdny.")
    @Size(max = 150, message = "Predmet emailu môže mať maximálne 150 znakov.")
    private String subject;

    @Size(max = 5000, message = "Správa emailu môže mať maximálne 5000 znakov.")
    private String message;

    private boolean sendToAll;

    private List<Long> clientIds;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSendToAll() {
        return sendToAll;
    }

    public void setSendToAll(boolean sendToAll) {
        this.sendToAll = sendToAll;
    }

    public List<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
    }
}
