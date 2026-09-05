package sk.patrikscerba.gym.service.email;

import sk.patrikscerba.gym.dto.email.EmailRequest;

/**
 * Definuje základné operácie pre odosielanie emailov.
 */
public interface EmailService {

    // Odošle HTML email.
    void sendEmail(EmailRequest emailRequest);
}
