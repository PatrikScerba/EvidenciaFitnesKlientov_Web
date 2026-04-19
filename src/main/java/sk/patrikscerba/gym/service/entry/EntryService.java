package sk.patrikscerba.gym.service.entry;

import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;

/**
 * Service rozhranie pre správu vstupov klientov.
 * Definuje operácie súvisiace s evidenciou vstupu.
 */
public interface EntryService {

    // Vytvorí nový záznam o vstupe klienta.
    EntryResponse createEntry(EntryCreateRequest request);

}

