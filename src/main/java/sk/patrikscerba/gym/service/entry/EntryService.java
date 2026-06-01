package sk.patrikscerba.gym.service.entry;

import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.enums.EntryMethod;

import java.util.List;

/**
 * Service rozhranie pre správu vstupov klientov.
 * Definuje operácie súvisiace s evidenciou vstupov a odchodov.
 */
public interface EntryService {

    // Vytvorí nový záznam o vstupe klienta.
    EntryResponse createEntry(EntryCreateRequest request);

    // Vytvorí nový záznam o vstupe klienta s konkrétnym spôsobom evidencie.
    EntryResponse createEntry(EntryCreateRequest request, EntryMethod arrivalMethod);

    // Zaznamená odchod klienta – nastaví čas odchodu pre jeho posledný aktívny vstup.
    EntryResponse registerDeparture(Long clientId);

    // Zaznamená odchod klienta s konkrétnym spôsobom evidencie odchodu.
    EntryResponse registerDeparture(Long clientId, EntryMethod departureMethod);

    // Vráti všetkých klientov, ktorí sú aktuálne v fitnescentre.
    List<EntryResponse> getActiveEntries();
}

