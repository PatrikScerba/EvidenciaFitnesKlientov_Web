package sk.patrikscerba.gym.service.qr;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service vrstva pre prácu s QR tokenom klienta.
 * Obsahuje logiku na generovanie QR tokenu.
 */
@Service
public class QrServiceImpl implements QrService {

    @Override
    public String generateQrToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
