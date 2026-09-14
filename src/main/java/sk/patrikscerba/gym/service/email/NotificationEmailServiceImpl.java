package sk.patrikscerba.gym.service.email;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailRequest;
import sk.patrikscerba.gym.dto.email.EmailSendRequest;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.exception.BusinessException;
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

        if (request.isSendToAll()
                && request.getClientIds() != null
                && !request.getClientIds().isEmpty()) {

            throw new BusinessException(
                    "Pri odosielaní všetkým klientom nesmú byť zadané konkrétne ID klientov."
            );
        }

        if (!request.isSendToAll()
                && (request.getClientIds() == null || request.getClientIds().isEmpty())) {

            throw new BusinessException(
                    "Musí byť zvolený aspoň jeden klient."
            );
        }

        List<ClientEntity> clients;

        if (request.isSendToAll()) {
            clients = clientRepository.findAll();
        } else {
            clients = clientRepository.findAllById(request.getClientIds());

            if (clients.size() != request.getClientIds().size()){
                throw new BusinessException(
                        "Jeden alebo viacerí zo zvolených klientov neexistujú."
                );
            }
        }

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
