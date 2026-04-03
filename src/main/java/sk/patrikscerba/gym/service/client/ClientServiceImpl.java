package sk.patrikscerba.gym.service.client;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.client.ClientCreateRequest;
import sk.patrikscerba.gym.dto.client.ClientResponse;
import sk.patrikscerba.gym.dto.client.ClientUpdateRequest;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.ConflictException;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.ClientRepository;
import sk.patrikscerba.gym.repository.UserRepository;

import java.util.List;

/**
 * Implementácia service vrstvy pre klientov.
 * Obsahuje biznis logiku pre registráciu, získanie, úpravu a vymazanie klienta.
 */
@Service
public class ClientServiceImpl implements ClientService {

    // Repository pre prácu s databázou klientov.
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    // Konštruktor na injektovanie repository vrstvy.
    public ClientServiceImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;

    }

    // Registrácia nového klienta
    @Override
    public ClientResponse registerClient(ClientCreateRequest request) {

        // Kontrola veku (biznis pravidlo)
        int age = java.time.Period.between(request.getDateOfBirth(), java.time.LocalDate.now()).getYears();
        if (age < 15) {
            throw new BusinessException("Registrácia nového klienta je dostupná len osobám starším ako 15 rokov.");
        }

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
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
                        new NotFoundException("Klient neexistuje, id=" + id)
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

    // Získanie klienta podľa emailu.
    @Override
    public ClientResponse getClientByEmail(String email) {

        ClientEntity entity = clientRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("Klient neexistuje pre email=" + email)
                );

        return mapToResponse(entity);
    }

    // Získanie údajov aktuálne prihláseného klienta podľa emailu z objektu Authentication.
    @Override
    public ClientResponse getMyClient(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("Používateľ neexistuje pre email=" + email)
                );

        if (user.getClient() == null) {
            throw new NotFoundException("Prihlásený používateľ nemá priradeného klienta.");
        }

        return mapToResponse(user.getClient());
    }

    // Aktualizácia existujúceho klienta podľa ID.
    @Override
    public ClientResponse updateClient(Long id, ClientUpdateRequest request) {

        int age = java.time.Period.between(request.getDateOfBirth(), java.time.LocalDate.now()).getYears();
        if (age < 15) {
            throw new BusinessException("Údaje klienta nie je možné upraviť, ak má osoba menej ako 15 rokov.");
        }

        ClientEntity entity = clientRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Klient neexistuje, id=" + id)
                );

        // Kontrola duplicity emailu iba v prípade, že sa email mení.
        if (!entity.getEmail().equals(request.getEmail())
                && clientRepository.existsByEmail(request.getEmail())) {

            throw new ConflictException(
                    "Email už používa iný klient: "
                            + request.getEmail()
            );
        }

        if (!entity.getEmail().equals(request.getEmail())) {

            userRepository.findByClient_ClientId(id)
                    .ifPresent(user -> {
                        user.setEmail(request.getEmail());
                        userRepository.save(user);
                    });
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
            throw new NotFoundException(
                    "Klient neexistuje, id=" + id);
        }

        // Vymazanie klienta z databázy.
        clientRepository.deleteById(id);
    }

    // Vyhľadávanie klienta
    public List<ClientResponse> searchClients(String firstName, String lastName, String email) {

        // Odstránenie medzier na začiatku a na konci.
        firstName = firstName == null ? null : firstName.trim();
        lastName = lastName == null ? null : lastName.trim();
        email = email == null ? null : email.trim();

        // Overenie či parametre boli zadané.
        boolean hasFirst = firstName != null && !firstName.isBlank();
        boolean hasLast = lastName != null && !lastName.isBlank();
        boolean hasEmail = email != null && !email.isBlank();

        // Ak je zadaný email, vyhľadáva sa prioritne podľa emailu
        if (hasEmail) {
            return clientRepository.findByEmail(email)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        List<ClientEntity> result;

        // Vyhľadávanie podľa mena/priezviska alebo oboje.
        if (hasFirst && hasLast) {
            result = clientRepository
                    .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);

        } else if (hasFirst) {
            result = clientRepository.findByFirstNameContainingIgnoreCase(firstName);

        } else if (hasLast) {
            result = clientRepository.findByLastNameContainingIgnoreCase(lastName);

        } else {
            result = clientRepository.findAll();
        }

        return result.stream()
                .map(this::mapToResponse)
                .toList();
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
