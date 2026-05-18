package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.qr.QrCodeResponse;
import sk.patrikscerba.gym.dto.qr.QrCodeShowRequest;
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

    // Získanie QR údajov aktuálne prihláseného klienta.
    @GetMapping("/me")
    public QrCodeResponse getMyQr(Authentication authentication) {
        return qrService.getMyQr(authentication.getName());
    }

    // Zobrazenie QR údajov klienta po overení bezpečnostnej odpovede.
    @PostMapping("/show")
    public QrCodeResponse showQrAfterSecurityAnswer(@Valid @RequestBody QrCodeShowRequest request) {
        return qrService.showQrAfterSecurityAnswer(request);
    }
}
