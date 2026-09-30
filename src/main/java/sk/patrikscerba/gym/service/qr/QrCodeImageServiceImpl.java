package sk.patrikscerba.gym.service.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.exception.BusinessException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrCodeImageServiceImpl implements QrCodeImageService {

    private static final int QR_SIZE = 270;

    @Override
    public byte[] generateQrCodeImage(String qrToken) {

        try {
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(qrToken, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ImageIO.write(qrImage, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (WriterException | IOException e) {
            throw new BusinessException(
                    "Nepodarilo sa vygenerovať QR kód: " + e.getMessage()
            );
        }
    }
}

