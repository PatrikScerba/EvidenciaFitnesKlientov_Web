package sk.patrikscerba.gym.serivce;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.ClientCreateRequest;
import sk.patrikscerba.gym.dto.ClientResponse;
import sk.patrikscerba.gym.dto.ClientUpdateRequest;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.repository.ClientRepository;

import java.util.List;

/**
 * Implementácia service vrstvy pre klientov.
 * Obsahuje biznis logiku pre registráciu, získanie, úpravu a vymazanie klienta.
 */
@Service
public class ClientServiceImpl implements ClientService {

    // Repository pre prácu s databázou klientov.
    private final ClientRepository clientRepository;

    // Konštruktor na injektovanie repository vrstvy.
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;

    }

    // Registrácia nového klienta.
    @Override
    public ClientResponse registerClient(ClientCreateRequest request) {

        // Kontrola veku (biznis pravidlo)
        int age = java.time.Period.between(request.getDateOfBirth(), java.time.LocalDate.now()).getYears();
        if (age < 15) {
            throw new RuntimeException("Registrácia nového klienta je dostupná len osobám starším ako 15 rokov.");
        }

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Klient s emailom už existuje: " + request.getEmail()
            );
        }

        // Vytvorenie entity z requestu.
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(request.getFirstName());
        clientEntity.setLastName(request.getLastName());
        clientEntity.setDateOfBirth(request.getDateOfBirth());
        clientEntity.setPhoneNumber(request.getPhoneNumber());
        clientEntity.setAddress(request.getAddress());
        clientEntity.setEmail(request.getEmail());

        ClientEntity saved = clientRepository.save(clientEntity);

        // Vrátenie odpovede vo forme DTO
        return mapToResponse(saved);
    }

    // Získanie klienta podľa ID.
    @Override
    public ClientResponse getClientById(Long id) {

        ClientEntity entity = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Klient neexistuje, id=" + id)
                );

        return mapToResponse(entity);
    }

    // Získanie všetkých klientov z databázy.
    @Override
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Aktualizácia existujúceho klienta podľa ID.
    @Override
    public ClientResponse updateClient(Long id, ClientUpdateRequest request) {

        ClientEntity entity = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Klient neexistuje, id=" + id)
                );

        // Kontrola duplicity emailu iba v prípade, že sa email mení.
        if (!entity.getEmail().equals(request.getEmail())
                && clientRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email už používa iný klient: "
                            + request.getEmail()
            );
        }

        // Prepísanie údajov klienta novými hodnotami z requestu.
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setDateOfBirth(request.getDateOfBirth());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setAddress(request.getAddress());
        entity.setEmail(request.getEmail());

        // Uloženie zmien do databázy.
        ClientEntity saved = clientRepository.save(entity);

        return mapToResponse(saved);
    }

    // Vymazanie klienta podľa ID.
    @Override
    public void deleteClient(Long id) {

        // kontrola existencie
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException(
                    "Klient neexistuje, id=" + id);
        }

        // Vymazanie klienta z databázy.
        clientRepository.deleteById(id);
    }

    // Pomocná metóda na prevod entity na response DTO.
    private ClientResponse mapToResponse(ClientEntity entity) {

        ClientResponse dto = new ClientResponse();

        dto.setClientId(entity.getClientId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());

        return dto;

    }
}
