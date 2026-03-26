package sk.patrikscerba.gym.service.client;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.client.ClientAccountResponse;
import sk.patrikscerba.gym.dto.client.ClientCreateRequest;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.enums.Role;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.ConflictException;
import sk.patrikscerba.gym.repository.ClientRepository;
import sk.patrikscerba.gym.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;

/**
 * Implementácia servisnej vrstvy pre registráciu klienta spolu s vytvorením používateľského účtu.
 * Trieda zabezpečuje validáciu veku klienta, kontrolu duplicity emailu, vytvorenie klienta,
 * založenie prihlasovacieho účtu s dočasným heslom a zostavenie odpovede pre frontend.
 */
@Service
public class ClientAccountServiceImpl implements ClientAccountService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientAccountServiceImpl(ClientRepository clientRepository,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Zaregistruje klienta, vytvorí mu používateľský účet a vráti odpoveď s dočasným heslom.
    @Override
    @Transactional
    public ClientAccountResponse registerClientWithAccount(ClientCreateRequest request) {

        validateClientAge(request.getDateOfBirth());

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Klient s emailom už existuje:" + request.getEmail());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Používateľ s emailom už existuje:" + request.getEmail());
        }

        // Zostavenie odpovede, ktorú backend pošle frontendu.
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(request.getFirstName());
        clientEntity.setLastName(request.getLastName());
        clientEntity.setDateOfBirth(request.getDateOfBirth());
        clientEntity.setPhoneNumber(request.getPhoneNumber());
        clientEntity.setAddress(request.getAddress());
        clientEntity.setEmail(request.getEmail());

        ClientEntity savedClient = clientRepository.save(clientEntity);

        // Vygenerovanie dočasného hesla pre nový účet.
        String temporaryPassword = generateTemporaryPassword();

        // Vytvorenie používateľského účtu naviazaného na uloženého klienta.
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(temporaryPassword));
        userEntity.setUsingTemporaryPassword(true);
        userEntity.setRole(Role.CLIENT);

        userEntity.setClient(savedClient);

        UserEntity savedUser = userRepository.save(userEntity);

        // Zostavenie odpovede, ktorú backend pošle frontendu.
        ClientAccountResponse response = new ClientAccountResponse();
        response.setClientId(savedClient.getClientId());
        response.setUserId(savedUser.getUserId());
        response.setFirstName(savedClient.getFirstName());
        response.setLastName(savedClient.getLastName());
        response.setEmail(savedClient.getEmail());
        response.setTemporaryPassword(temporaryPassword);
        response.setRole(savedUser.getRole().name());
        response.setMessage("Klient aj prihlasovací účet boli úspešne vytvorené.");

        return response;
    }

    // Overenie veku klienta podľa dátumu narodenia.
    private void validateClientAge(LocalDate dateOfBirth) {
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();

        if (age < 15) {
            throw new BusinessException("Registrácia nového klienta je dostupná len osobám vo veku 15 rokov a viac.");
        }
    }

    // Vygeneruje náhodné dočasné heslo pre nový používateľský účet.
    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}