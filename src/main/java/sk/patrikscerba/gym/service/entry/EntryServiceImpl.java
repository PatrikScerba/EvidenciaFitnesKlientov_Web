package sk.patrikscerba.gym.service.entry;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.dto.membership.MembershipResponse;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.entity.EntryEntity;
import sk.patrikscerba.gym.enums.EntryStatus;
import sk.patrikscerba.gym.enums.MembershipStatus;
import sk.patrikscerba.gym.enums.Reason;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.ClientRepository;
import sk.patrikscerba.gym.repository.EntryRepository;
import sk.patrikscerba.gym.service.membership.MembershipService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Implementácia service logiky pre evidenciu vstupov klientov.
 * Zabezpečuje vytvorenie nového vstupu a vyhodnotenie,
 * či môže byť vstup schválený alebo zamietnutý.
 */
@Service
public class EntryServiceImpl implements EntryService {

    private final ClientRepository clientRepository;
    private final EntryRepository entryRepository;
    private final MembershipService membershipService;

    public EntryServiceImpl(ClientRepository clientRepository,
                            EntryRepository entryRepository,
                            MembershipService membershipService) {
        this.clientRepository = clientRepository;
        this.entryRepository = entryRepository;
        this.membershipService = membershipService;
    }

    // Vytvorí nový záznam o vstupe klienta a zároveň vyhodnotí,
    // či má byť vstup schválený alebo zamietnutý.
    @Override
    @Transactional
    public EntryResponse createEntry(EntryCreateRequest request) {

        // Načítanie klienta podľa ID zo vstupného requestu.
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new NotFoundException("Klient s daným ID neexistuje."));

        // Príprava aktuálneho dátumu a časového rozsahu dnešného dňa.
        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDate today = now.toLocalDate();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // Predvolený stav je schválený vstup bez obmedzenia.
        EntryStatus status = EntryStatus.APPROVED;
        Reason reason = Reason.OK;

        // Kontrola permanentky
        try {
            MembershipResponse membership = membershipService.getMembershipByClientId(client.getClientId());

            if (membership.getStatus() != MembershipStatus.ACTIVE) {
                status = EntryStatus.DENIED;
                reason = Reason.MEMBERSHIP_EXPIRED;
            }
        } catch (NotFoundException e) {
            status = EntryStatus.DENIED;
            reason = Reason.NO_MEMBERSHIP;
        }

        // Kontrola vekového obmedzenia.
        if (status == EntryStatus.APPROVED && client.getDateOfBirth() != null) {
            int age = Period.between(client.getDateOfBirth(), today).getYears();

            if (age < 15) {
                status = EntryStatus.DENIED;
                reason = Reason.AGE_RESTRICTION;
            }
        }

        // Kontrola, či už klient nemal dnes schválený vstup.
        if (status == EntryStatus.APPROVED) {
            boolean alreadyHasApprovedEntryToday =
                    entryRepository.existsByClientAndArrivalTimeBetweenAndStatus(
                            client,
                            startOfDay,
                            endOfDay,
                            EntryStatus.APPROVED
                    );

            if (alreadyHasApprovedEntryToday) {
                status = EntryStatus.DENIED;
                reason = Reason.ALREADY_ENTERED_TODAY;
            }
        }

        // Vytvorenie nového záznamu vstupu.
        EntryEntity entry = new EntryEntity();
        entry.setClient(client);
        entry.setStatus(status);
        entry.setReason(reason);
        entry.setArrivalTime(now);
        entry.setDepartureTime(null);
        entry.setNote(request.getNote());

        EntryEntity savedEntry = entryRepository.save(entry);

        return mapToResponse(savedEntry);
    }

    // Zaznamená odchod klienta, t.j. nastaví čas odchodu pre aktívny vstup.
    @Override
    @Transactional
    public EntryResponse registerDeparture(Long clientId) {

        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Klient s daným ID neexistuje."));

        EntryEntity entry = entryRepository
                .findTopByClientAndStatusAndDepartureTimeIsNullOrderByArrivalTimeDesc(
                        client,
                        EntryStatus.APPROVED
                )
                .orElseThrow(() -> new NotFoundException("Klient nemá žiadny aktívny vstup na odchod."));

        entry.setDepartureTime(LocalDateTime.now().withNano(0));

        EntryEntity savedEntry = entryRepository.save(entry);

        return mapToResponse(savedEntry);
    }

    // Prevedie entitu vstupu na výstupný DTO objekt.
    private EntryResponse mapToResponse(EntryEntity entry) {
        EntryResponse response = new EntryResponse();
        response.setId(entry.getId());
        response.setClientId(entry.getClient().getClientId());
        response.setFirstName(entry.getClient().getFirstName());
        response.setLastName(entry.getClient().getLastName());
        response.setStatus(entry.getStatus());
        response.setReason(entry.getReason());
        response.setArrivalTime(entry.getArrivalTime().toLocalTime().withNano(0));
        response.setDate(entry.getArrivalTime().toLocalDate());

        if (entry.getDepartureTime() != null) {
            response.setDepartureTime(entry.getDepartureTime().toLocalTime().withNano(0));
        }

        response.setNote(entry.getNote());

        return response;
    }
}


