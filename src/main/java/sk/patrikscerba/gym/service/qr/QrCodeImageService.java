package sk.patrikscerba.gym.service.qr;

/**
 * Servisné rozhranie pre generovanie QR kódu vo forme obrázka.
 */
public interface QrCodeImageService {

    byte[] generateQrCodeImage(String qrToken);

}
