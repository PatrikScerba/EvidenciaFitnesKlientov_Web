package sk.patrikscerba.gym.service.qr;

/**
 * Service rozhranie pre prácu s QR tokenom klienta.
 */
public interface QrService {

    // Vygeneruje nový unikátny QR token.
    String generateQrToken();
}
