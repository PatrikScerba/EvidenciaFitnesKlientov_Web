package sk.patrikscerba.gym.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import sk.patrikscerba.gym.dto.email.EmailRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Implementácia služby pre odosielanie HTML e-mailových správ.
 * Využíva Thymeleaf šablónu a JavaMailSender na vytvorenie
 * výslednej e-mailovej správy a jej odoslanie
 * cez nakonfigurovaný SMTP server.
 */
@Service
public class EmailServiceImpl implements EmailService {

    // Názov odosielateľa zobrazovaný príjemcovi e-mailu.
    private static final String SENDER_NAME = "Gym Management System";

    // Maximálna povolená celková veľkosť príloh e-mailu (25 MB).
    private static final long MAX_ATTACHMENT_SIZE = 25L * 1024 * 1024;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    // E-mailová adresa odosielateľa načítaná z konfigurácie aplikácie.
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    // Vytvorí a odošle HTML e-mail podľa údajov z prijatej požiadavky a voliteľne k nemu pripojí zadané prílohy.
    @Override
    public void sendEmail(EmailRequest emailRequest,
                          List<MultipartFile> attachments
    ) {
        boolean hasAttachment =
                attachments != null && !attachments.isEmpty();

        if (hasAttachment) {

            // Spočíta celkovú veľkosť všetkých príloh a overí povolený limit.
            long totalAttachmentsSize = 0;

            for (MultipartFile attachment : attachments) {
                totalAttachmentsSize += attachment.getSize();
            }
            if (totalAttachmentsSize > MAX_ATTACHMENT_SIZE) {

                throw new IllegalArgumentException(
                        "Celková veľkosť príloh nemôže byť väčšia ako 25 MB."
                );
            }
        }

        try {

            // Pripraví údaje, ktoré budú použité v Thymeleaf šablóne.
            Context context = new Context();
            context.setVariable("recipientName", emailRequest.getRecipientName());
            context.setVariable("message", emailRequest.getMessage());
            context.setVariable("hasAttachment", hasAttachment);

            // Vytvorí výsledný HTML obsah e-mailu z Thymeleaf šablóny.
            String htmlContent = templateEngine.process("email/notification-email", context);

            // Vytvorí MIME správu s podporou HTML obsahu a vložených zdrojov.
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Načíta logo aplikácie, ktoré bude vložené priamo do HTML e-mailu.
            ClassPathResource logo = new ClassPathResource("static/images/GMS-logo.png");

            // Nastaví odosielateľa, príjemcu, predmet, HTML obsah a vložené logo.
            helper.setFrom(fromEmail, SENDER_NAME);
            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(htmlContent, true);
            helper.addInline("logo", logo);


            if (hasAttachment) {
                for (MultipartFile attachment : attachments) {
                    String attachmentName = attachment.getOriginalFilename();

                    if (attachmentName == null || attachmentName.isBlank()) {
                        attachmentName = "attachment";
                    }

                    helper.addAttachment(
                            attachmentName,
                            attachment
                    );
                }
            }

            javaMailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Nepodarilo sa odoslať HTML email.", e);
        }
    }
}

