package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.entry.EntryCreateRequest;
import sk.patrikscerba.gym.dto.entry.EntryResponse;
import sk.patrikscerba.gym.service.entry.EntryService;

/**
 * Controller pre evidenciu vstupov klientov.
 * Zabezpečuje vytvorenie nového záznamu o vstupe.
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
}


