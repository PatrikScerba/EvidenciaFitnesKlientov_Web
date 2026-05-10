package sk.patrikscerba.gym.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.patrikscerba.gym.dto.entry.EntryQrRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.service.entry.EntryScanService;

/**
 * Controller pre spracovanie QR scan flowu.
 * Automaticky rozhoduje, či QR scan znamená vstup alebo odchod klienta.
 */
@RestController
@RequestMapping("/api/entries/scan")
public class EntryScanController {

    private final EntryScanService entryScanService;

    public EntryScanController(EntryScanService entryScanService) {
        this.entryScanService = entryScanService;
    }

    // Spracuje QR scan a service vrstva rozhodne,
    // či klient prichádza alebo odchádza.
    @PostMapping
    public ResponseEntity<EntryResponse> scanQrCode(@Valid @RequestBody EntryQrRequest request) {
        EntryResponse response = entryScanService.scanQrCode(request);
        return ResponseEntity.ok(response);
    }
}

