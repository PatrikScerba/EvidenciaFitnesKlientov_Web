package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.entry.EntryQrRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.service.entry.EntryScanService;

/**
 * Controller pre spracovanie QR tokenov v servisnom QR režime.
 * Slúži na spracovanie QR tokenov mimo automatizovaného turniketového režimu.
 * Systém na základe QR tokenu vyhodnotí stav klienta a rozhodne výsledok spracovania.
 */
@RestController
@RequestMapping("/api/entries/qr")
public class EntryQrController {

    private final EntryScanService entryScanService;

    public EntryQrController(EntryScanService entryScanService) {
        this.entryScanService = entryScanService;
    }

    // Spracuje QR token v servisnom režime.
    // Systém vyhodnotí stav klienta a rozhodne výsledok spracovania.
    @PostMapping("/service-scan")
    public ResponseEntity<EntryResponse> processQrServiceScan(@Valid @RequestBody EntryQrRequest request) {
        return ResponseEntity.ok(entryScanService.scanQrCodeServiceMode(request));
    }
}
