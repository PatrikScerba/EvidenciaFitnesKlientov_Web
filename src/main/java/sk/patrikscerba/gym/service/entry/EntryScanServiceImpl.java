package sk.patrikscerba.gym.service.entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryQrRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.enums.EntryMethod;
import sk.patrikscerba.gym.enums.EntryStatus;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.ClientRepository;
import sk.patrikscerba.gym.repository.EntryRepository;

/**
 * Implementácia scan logiky pre QR príchod a odchod klienta.
 * Rozhoduje, či QR scan znamená príchod alebo odchod.
 */
@Service
public class EntryScanServiceImpl implements EntryScanService {

    private final EntryService entryService;
    private final EntryRepository entryRepository;
    private final ClientRepository clientRepository;

    public EntryScanServiceImpl(EntryService entryService, EntryRepository entryRepository, ClientRepository clientRepository) {
        this.entryService = entryService;
        this.entryRepository = entryRepository;
        this.clientRepository = clientRepository;
    }

    // Spracuje QR scan a podľa aktívneho vstupu rozhodne,
    // či klient prichádza alebo odchádza.
    @Override
    @Transactional
    public EntryResponse scanQrCode(EntryQrRequest request) {

        ClientEntity client = clientRepository.findByQrToken(request.getQrToken())
                .orElseThrow(() -> new NotFoundException("Neplatný QR kód"));

        boolean hasOpenEntry = entryRepository.existsByClientAndStatusAndDepartureTimeIsNull(
                client,
                EntryStatus.APPROVED
        );

        if (hasOpenEntry) {
            return entryService.registerDeparture(client.getClientId(), EntryMethod.QR_SCAN);
        }

        EntryCreateRequest newRequest = new EntryCreateRequest();
        newRequest.setClientId(client.getClientId());
        newRequest.setNote(null);

        return entryService.createEntry(newRequest, EntryMethod.QR_SCAN);
    }
}

