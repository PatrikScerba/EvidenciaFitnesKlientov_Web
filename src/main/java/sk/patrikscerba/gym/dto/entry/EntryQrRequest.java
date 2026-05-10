package sk.patrikscerba.gym.dto.entry;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO objekt pre prijatie QR tokenu pri vytvorení vstupu cez QR kód.
 */
public class EntryQrRequest {

    @NotBlank(message = "QR token nesmie byť prázdny.")
    private String qrToken;

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }
}

