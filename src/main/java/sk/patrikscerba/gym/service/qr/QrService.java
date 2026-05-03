package sk.patrikscerba.gym.service.qr;

import sk.patrikscerba.gym.dto.qr.QrCodeResponse;

/**
 * Service rozhranie pre prácu s QR tokenom klienta.
 * Definuje základné operácie na vytvorenie a získanie QR údajov klienta.
 */
public interface QrService {

    // Vygeneruje nový unikátny QR token.
    String generateQrToken();

    // Získa QR údaje konkrétneho klienta podľa jeho ID.
    QrCodeResponse getQrForClient(Long clientId);

    // Získa QR údaje aktuálne prihláseného klienta podľa emailu.
    QrCodeResponse getMyQr(String email);
}
