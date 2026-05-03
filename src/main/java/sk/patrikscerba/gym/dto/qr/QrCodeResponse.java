package sk.patrikscerba.gym.dto.qr;

/**
 * DTO trieda reprezentujúca odpoveď s QR údajmi klienta.
 * Používa sa pri odosielaní QR tokenu a základných údajov klienta na frontend.
 */
public class QrCodeResponse {

    private Long clientId;
    private String firstName;
    private String lastName;
    private String qrToken;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

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

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }
}

