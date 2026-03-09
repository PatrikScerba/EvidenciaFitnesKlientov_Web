package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.ClientCreateRequest;
import sk.patrikscerba.gym.dto.ClientResponse;
import sk.patrikscerba.gym.dto.ClientUpdateRequest;
import sk.patrikscerba.gym.service.ClientService;

import java.util.List;

/**
 * Základná URL pre všetky endpointy v tomto controllery
 * Všetko čo je tu definované bude mať prefix /clients
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Endpoint na registráciu nového klienta.
    // @Valid = spustí validácie DTO (NotBlank, Email, ...)
    @PostMapping
    public ResponseEntity<ClientResponse> registerClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse response = clientService.registerClient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Vráti zoznam všetkých klientov.
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<ClientResponse> clients = clientService.getAllClients();

        return ResponseEntity.ok(clients);
    }

    // Vráti detail klienta podľa ID.
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(
            @PathVariable Long id   // ID z URL
    ) {
        return ResponseEntity.ok(
                clientService.getClientById(id)
        );
    }

    // Aktualizácia existujúceho klienta.
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientUpdateRequest request) {

        return ResponseEntity.ok(
                clientService.updateClient(id, request)
        );
    }

    // Vymazanie klienta podľa ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id
    ) {
        clientService.deleteClient(id);

        return ResponseEntity.noContent().build();
    }
}
