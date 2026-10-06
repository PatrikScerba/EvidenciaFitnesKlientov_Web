package sk.patrikscerba.gym.service.email;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.email.EmailRequest;
import sk.patrikscerba.gym.enums.EmailTemplate;
import sk.patrikscerba.gym.service.qr.QrCodeImageService;

@Service
public class SystemEmailServiceImpl implements SystemEmailService {

    private final EmailService emailService;
    private final QrCodeImageService qrCodeImageService;


    public SystemEmailServiceImpl(EmailService emailService,
                                  QrCodeImageService qrCodeImageService) {
        this.emailService = emailService;
        this.qrCodeImageService = qrCodeImageService;
    }

    @Override
    public void sendRegistrationConfirmation(
            String email, String firstName, String lastName, String qrToken) {

        byte[] qrImage = qrCodeImageService.generateQrCodeImage(qrToken);

        EmailRequest emailRequest = new EmailRequest();

        emailRequest.setTo(email);
        emailRequest.setRecipientName(firstName + " " + lastName);
        emailRequest.setSubject("Potvrdenie registrácie do Gym Management System");
        emailRequest.setMessage(
                "Ďakujeme za registráciu. Vaše konto bolo úspešne vytvorené."

        );

        emailService.sendEmailWithQr(emailRequest, qrImage, EmailTemplate.REGISTRATION);
    }

    @Override
    public void sendQrResetConfirmation(
            String email,
            String firstName,
            String lastName,
            String qrToken) {

        // Z aktuálneho QR tokenu vygeneruje nový QR obrázok.
        byte[] qrImage = qrCodeImageService.generateQrCodeImage(qrToken);

        EmailRequest emailRequest = new EmailRequest();

        emailRequest.setTo(email);
        emailRequest.setRecipientName(firstName + " " + lastName);
        emailRequest.setSubject("Nový QR kód - Gym Management System");
        emailRequest.setMessage(
                "Váš QR kód bol úspešne obnovený. " +
                        "Nový QR kód nájdete v prílohe tohto e-mailu."
        );

        emailService.sendEmailWithQr(emailRequest, qrImage, EmailTemplate.QR_RESET);
    }
}

