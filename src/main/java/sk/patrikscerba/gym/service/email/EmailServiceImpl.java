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

@Service
public class EmailServiceImpl implements EmailService {

    private static final String SENDER_NAME = "Gym Management System";

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    // Odošle HTML e-mail na základe údajov z prijatej požiadavky.
    @Override
    public void sendEmail(EmailRequest emailRequest,
                          List<MultipartFile> attachments
    ) {
        boolean hasAttachment =
                attachments != null && !attachments.isEmpty();

        try {
            Context context = new Context();
            context.setVariable("recipientName", emailRequest.getRecipientName());
            context.setVariable("message", emailRequest.getMessage());
            context.setVariable("hasAttachment", hasAttachment);

            String htmlContent = templateEngine.process("email/notification-email", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            ClassPathResource logo = new ClassPathResource("static/images/GMS-logo.png");

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

