package sk.patrikscerba.gym.service.email;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sk.patrikscerba.gym.dto.email.EmailRequest;
import sk.patrikscerba.gym.dto.email.EmailSendRequest;
import sk.patrikscerba.gym.dto.email.EmailSendResult;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.repository.ClientRepository;

import java.util.List;

/**
 * Implementácia služby pre odosielanie e-mailových správ klientom.
 * Spracováva výber príjemcov, overuje platnosť požiadavky
 * a deleguje samotné odoslanie jednotlivých e-mailov
 * na EmailService.
 */
@Service
public class NotificationEmailServiceImpl implements NotificationEmailService {

    private final EmailService emailService;
    private final ClientRepository clientRepository;

    public NotificationEmailServiceImpl(EmailService emailService,
                                        ClientRepository clientRepository) {
        this.emailService = emailService;
        this.clientRepository = clientRepository;
    }

    // Spracuje požiadavku na odoslanie e-mailu vybraným alebo všetkým klientom.
    @Override
    public EmailSendResult sendEmail(EmailSendRequest request, List<MultipartFile> attachments) {

        // Zabráni kombinácii hromadného odoslania so zoznamom konkrétnych klientov.
        if (request.isSendToAll()
                && request.getClientIds() != null
                && !request.getClientIds().isEmpty()) {

            throw new BusinessException(
                    "Pri odosielaní všetkým klientom nesmú byť zadané konkrétne ID klientov."
            );
        }

        // Pri cielenom odosielaní vyžaduje výber aspoň jedného klienta.
        if (!request.isSendToAll()
                && (request.getClientIds() == null || request.getClientIds().isEmpty())) {

            throw new BusinessException(
                    "Musí byť zvolený aspoň jeden klient."
            );
        }

        List<ClientEntity> clients;

        // Načíta všetkých klientov alebo iba klientov vybraných v požiadavke.
        if (request.isSendToAll()) {
            clients = clientRepository.findAll();
        } else {
            clients = clientRepository.findAllById(request.getClientIds());

            // Overí, že boli nájdení všetci klienti uvedení v požiadavke.
            if (clients.size() != request.getClientIds().size()) {
                throw new BusinessException(
                        "Jeden alebo viacerí zo zvolených klientov neexistujú."
                );
            }
        }

        // Zabráni odosielaniu, ak nebol nájdený žiadny príjemca.
        if (clients.isEmpty()) {
            throw new BusinessException(
                    "Nenašiel sa žiadny klient pre odoslanie emailu."
            );
        }

        // Pre každého príjemcu pripraví individuálnu e-mailovú požiadavku
        // a pokúsi sa ju odoslať prostredníctvom EmailService.
        // Eviduje úspešne a neúspešné pokusy a pri chybe pokračuje ďalším klientom.
        int successful = 0;
        int failed = 0;

        for (ClientEntity client : clients) {
            try {
                EmailRequest emailRequest = new EmailRequest();

                emailRequest.setTo(client.getEmail());
                emailRequest.setRecipientName(client.getFirstName());
                emailRequest.setSubject(request.getSubject());
                emailRequest.setMessage(request.getMessage());

                emailService.sendEmail(emailRequest, attachments);
                successful++;

            } catch (RuntimeException e) {
                failed++;
                System.err.println(
                        "Odosielanie emailu zlyhalo pre klienta ID: "
                                + client.getClientId()
                                + " - " + e.getMessage()
                );
            }
        }
        return new EmailSendResult(clients.size(), successful, failed);
    }
}

