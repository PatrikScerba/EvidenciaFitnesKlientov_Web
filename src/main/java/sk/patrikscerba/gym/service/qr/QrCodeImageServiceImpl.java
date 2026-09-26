package sk.patrikscerba.gym.service.qr;

import org.springframework.stereotype.Service;

@Service
public class QrCodeImageServiceImpl implements QrCodeImageService {

    private static final int QR_SIZE = 270;

    @Override
    public byte[] generateQrCodeImage(String qrToken) {

        return null;
    }
}