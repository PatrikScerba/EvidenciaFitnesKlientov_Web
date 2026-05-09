package sk.patrikscerba.gym.service.entry;

import sk.patrikscerba.gym.dto.entry.EntryQrRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;

/**
 * Service rozhranie pre spracovanie QR scanu pri vstupe alebo odchode klienta.
 */
public interface EntryScanService {

    // Spracuje QR scan a rozhodne, či ide o príchod alebo odchod klienta.
    EntryResponse scanQrCode(EntryQrRequest request);
}

