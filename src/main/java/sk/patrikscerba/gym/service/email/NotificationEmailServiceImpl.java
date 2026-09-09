package sk.patrikscerba.gym.service.email;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailRequest;
import sk.patrikscerba.gym.dto.email.EmailSendRequest;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.repository.ClientRepository;

import java.util.List;

@Service
public class NotificationEmailServiceImpl implements NotificationEmailService {

    private final EmailService emailService;
    private final ClientRepository clientRepository;

    public NotificationEmailServiceImpl(EmailService emailService,
                                        ClientRepository clientRepository) {
        this.emailService = emailService;
        this.clientRepository = clientRepository;
    }

    @Override
    public void sendEmail(EmailSendRequest request, List<MultipartFile> attachments) {

        List<ClientEntity> clients =
                clientRepository.findAllById(request.getClientIds());

        for (ClientEntity client : clients) {
            EmailRequest emailRequest = new EmailRequest();

            emailRequest.setTo(client.getEmail());
            emailRequest.setRecipientName(client.getFirstName());
            emailRequest.setSubject(request.getSubject());
            emailRequest.setMessage(request.getMessage());

            emailService.sendEmail(emailRequest, attachments);
        }
    }
}
