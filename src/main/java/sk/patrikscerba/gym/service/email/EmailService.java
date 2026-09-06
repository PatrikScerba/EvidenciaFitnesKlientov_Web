package sk.patrikscerba.gym.service.email;

import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailRequest;

import java.util.List;

/**
 * Definuje základné operácie pre odosielanie emailov.
 */
public interface EmailService {

    // Odošle HTML email.
    void sendEmail(EmailRequest emailRequest,
                   List<MultipartFile> attachments);
}
