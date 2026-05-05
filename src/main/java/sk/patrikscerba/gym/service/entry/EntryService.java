package sk.patrikscerba.gym.service.entry;

import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryQrRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;

import java.util.List;

/**
 * Service rozhranie pre správu vstupov klientov.
 * Definuje operácie súvisiace s evidenciou vstupu.
 */
public interface EntryService {

    // Vytvorí nový záznam o vstupe klienta.
    EntryResponse createEntry(EntryCreateRequest request);

    // Zaznamená odchod klienta – nastaví čas odchodu pre jeho posledný aktívny vstup.
    EntryResponse registerDeparture(Long clientId);

    // Vráti všetkých klientov, ktorí sú aktuálne v fitnescentre.
    List<EntryResponse> getActiveEntries();

    // Vytvorí vstup klienta podľa QR tokenu a využije existujúcu vstupnú logiku.
    EntryResponse createEntryByQr(EntryQrRequest request);

    // Zaznamená odchod klienta podľa QR tokenu a využije existujúcu logiku odchodu.
    EntryResponse registerDepartureByQr(String qrToken);
}

