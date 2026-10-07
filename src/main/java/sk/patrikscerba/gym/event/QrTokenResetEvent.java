package sk.patrikscerba.gym.event;

/**
 * Udalosť vytvorená po úspešnom resete QR tokenu klienta.
 */
public record QrTokenResetEvent(
        String email,
        String firstName,
        String lastName,
        String qrToken
) {
}