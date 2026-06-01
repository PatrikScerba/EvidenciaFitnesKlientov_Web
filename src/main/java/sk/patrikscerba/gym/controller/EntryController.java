package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.service.entry.EntryService;

import java.util.List;

/**
 * Controller pre manuálnu evidenciu vstupov a odchodov klientov.
 * Umožňuje vytvárať vstupy, zaznamenávať odchody a zobraziť aktívne vstupy klientov.
 */
@RestController
@RequestMapping("/api/entries")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    // Manuálne vytvorí nový záznam o vstupe klienta podľa jeho ID.
    // Service vrstva rozhodne, či bude vstup povolený alebo zamietnutý.
    @PostMapping
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody EntryCreateRequest request) {
        EntryResponse response = entryService.createEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Manuálne zaznamená odchod klienta podľa jeho ID.
    // Používa sa vtedy, keď zamestnanec alebo admin explicitne vyberie klienta.
    @PatchMapping("/{clientId}/departure")
    public ResponseEntity<EntryResponse> registerDeparture(@PathVariable Long clientId) {
        EntryResponse response = entryService.registerDeparture(clientId);
        return ResponseEntity.ok(response);
    }

    // Získa zoznam všetkých aktívnych vstupov (schválených a bez zaznamenaného odchodu).
    @GetMapping("/active")
    public ResponseEntity<List<EntryResponse>> getActiveEntries() {
        List<EntryResponse> activeEntries = entryService.getActiveEntries();
        return ResponseEntity.ok(activeEntries);
    }
}


