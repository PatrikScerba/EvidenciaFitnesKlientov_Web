package sk.patrikscerba.gym.service.email;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.email.EmailRequest;

@Service
public class SystemEmailServiceImpl implements SystemEmailService {

    private final EmailService emailService;


    public SystemEmailServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendRegistrationConfirmation(
            String email, String firstName, String lastName) {

        EmailRequest emailRequest = new EmailRequest();

        emailRequest.setTo(email);
        emailRequest.setRecipientName(firstName + " " + lastName);
        emailRequest.setSubject("Potvrdenie registrácie do Gym Management System");
        emailRequest.setMessage(
                "Ďakujeme za registráciu. Vaše konto bolo úspešne vytvorené."

        );

        emailService.sendEmail(emailRequest, null);

    }
}

