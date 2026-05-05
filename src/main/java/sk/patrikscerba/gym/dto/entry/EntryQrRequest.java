package sk.patrikscerba.gym.dto.entry;

/**
 * DTO objekt pre prijatie QR tokenu pri vytvorení vstupu cez QR kód.
 */
public class EntryQrRequest {

    private String qrToken;

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }
}

