package sk.patrikscerba.gym.service.qr;

import sk.patrikscerba.gym.dto.qr.QrCodeResponse;
import sk.patrikscerba.gym.dto.qr.QrTokenResetRequest;

/**
 * Service rozhranie pre regeneráciu QR tokenu klienta.
 * Definuje operáciu bezpečného resetovania QR tokenu.
 */
public interface QrTokenResetService {

    // Overí bezpečnostnú odpoveď a vygeneruje nový QR token klienta.
    QrCodeResponse resetQrToken(QrTokenResetRequest request);
}
