package sk.patrikscerba.gym.dto.entry;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO objekt pre vytvorenie nového vstupu klienta.
 * Obsahuje údaje, ktoré prídu z frontendu pri zaznamenaní vstupu.
 */
public class EntryCreateRequest {


    @NotNull(message = "ID klienta je povinné.")
    private Long clientId;

    @Size(max = 255, message = "Poznámka môže mať maximálne 255 znakov.")
    private String note;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}