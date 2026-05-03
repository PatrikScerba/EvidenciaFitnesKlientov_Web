package sk.patrikscerba.gym.service.qr;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.qr.QrCodeResponse;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.ClientRepository;

import java.util.UUID;

/**
 * Service vrstva pre prácu s QR tokenom klienta.
 * Obsahuje logiku na generovanie QR tokenu a získanie QR údajov klienta.
 */
@Service
public class QrServiceImpl implements QrService {

    private final ClientRepository clientRepository;

    public QrServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Vygeneruje nový unikátny QR token bez pomlčiek.
    @Override
    public String generateQrToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // Získa QR údaje konkrétneho klienta podľa jeho ID.
    @Override
    public QrCodeResponse getQrForClient(Long clientId) {

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Klient sa nenašiel"));

        return mapToResponse(client);
    }

    // Získa QR údaje aktuálne prihláseného klienta podľa emailu.
    @Override
    public QrCodeResponse getMyQr(String email) {

        ClientEntity client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Klient sa nenašiel"));

        return mapToResponse(client);
    }

    // Premapuje entitu klienta na DTO odpoveď pre frontend.
    private QrCodeResponse mapToResponse(ClientEntity client) {

        QrCodeResponse response = new QrCodeResponse();
        response.setClientId(client.getClientId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setQrToken(client.getQrToken());

        return response;
    }
}

