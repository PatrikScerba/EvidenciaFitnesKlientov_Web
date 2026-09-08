package sk.patrikscerba.gym.service.email;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.repository.ClientRepository;

@Service
public class NotificationEmailServiceImpl implements NotificationEmailService {

    private final EmailService emailService;
    private final ClientRepository clientRepository;

    public NotificationEmailServiceImpl(EmailService emailService,
                                        ClientRepository clientRepository) {
        this.emailService = emailService;
        this.clientRepository = clientRepository;
    }
}