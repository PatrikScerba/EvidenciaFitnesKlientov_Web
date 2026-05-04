package sk.patrikscerba.gym.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.patrikscerba.gym.dto.qr.QrCodeResponse;
import sk.patrikscerba.gym.service.qr.QrService;

/**
 * Controller pre prácu s QR kódom klienta.
 * Spracováva požiadavky na získanie QR údajov podľa klienta
 * alebo pre aktuálne prihláseného používateľa.
 */
@RestController
@RequestMapping("/api/qr")
public class QrController {

    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }

    // Získanie QR údajov konkrétneho klienta podľa jeho ID.
    @GetMapping("/client/{clientId}")
    public QrCodeResponse getQrForClient(@PathVariable Long clientId) {
        return qrService.getQrForClient(clientId);
    }

    // Získanie QR údajov aktuálne prihláseného klienta.
    @GetMapping("/me")
    public QrCodeResponse getMyQr(Authentication authentication) {
        return qrService.getMyQr(authentication.getName());
    }
}
