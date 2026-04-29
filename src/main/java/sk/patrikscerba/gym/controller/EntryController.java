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
 * Controller pre evidenciu vstupov klientov.
 * Zabezpečuje vytváranie záznamov o vstupe a zaznamenanie odchodu klienta.
 */
@RestController
@RequestMapping("/api/entries")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    // Vytvorí nový záznam o vstupe klienta.
    // Service vrstva rozhodne, či bude vstup povolený alebo zamietnutý.
    @PostMapping
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody EntryCreateRequest request) {
        EntryResponse response = entryService.createEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Zaznamená odchod klienta na poslednom aktívnom schválenom vstupe.
    @PatchMapping("/{clientId}/departure")
    public ResponseEntity<EntryResponse> registerDeparture(@PathVariable Long clientId) {
        EntryResponse response = entryService.registerDeparture(clientId);
        return ResponseEntity.ok(response);
    }

    // Získá zoznam všetkých aktívnych vstupov (schválených a bez zaznamenaného odchodu).
    @GetMapping("/active")
    public ResponseEntity<List<EntryResponse>> getActiveEntries() {
        List<EntryResponse> activeEntries = entryService.getActiveEntries();
        return ResponseEntity.ok(activeEntries);
    }
}


