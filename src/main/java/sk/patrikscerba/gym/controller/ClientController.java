package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.client.ClientAccountResponse;
import sk.patrikscerba.gym.dto.client.ClientCreateRequest;
import sk.patrikscerba.gym.dto.client.ClientResponse;
import sk.patrikscerba.gym.dto.client.ClientUpdateRequest;
import sk.patrikscerba.gym.service.client.ClientAccountService;
import sk.patrikscerba.gym.service.client.ClientService;

import java.util.List;

/**
 * REST controller pre správu klientov.
 * Všetky endpointy v tejto triede majú základný prefix /clients.
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientAccountService clientAccountService;

    public ClientController(ClientService clientService, ClientAccountService clientAccountService) {
        this.clientService = clientService;
        this.clientAccountService = clientAccountService;
    }

    // Endpoint na registráciu nového klienta a zároveň vytvorenie používateľského účtu.
    // @Valid = spustí validácie DTO (NotBlank, Email, ...)
    @PostMapping
    public ResponseEntity<ClientAccountResponse> registerWithAccount(@Valid @RequestBody ClientCreateRequest request) {
        ClientAccountResponse response = clientAccountService.registerClientWithAccount(request);

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
            @PathVariable Long id
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

    // Vyhľadávanie klienta.
    @GetMapping("/search")
    public ResponseEntity<List<ClientResponse>> searchClients(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email
    ) {
        return ResponseEntity.ok(clientService.searchClients(firstName, lastName, email));
    }
}
