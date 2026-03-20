package sk.patrikscerba.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.EmployeeAccountResponse;
import sk.patrikscerba.gym.dto.EmployeeCreateRequest;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.enums.Role;
import sk.patrikscerba.gym.exception.ConflictException;
import sk.patrikscerba.gym.repository.UserRepository;

import java.security.SecureRandom;

/**
 * Implementácia servisnej vrstvy pre vytvorenie účtu zamestnanca.
 * Trieda zabezpečuje kontrolu duplicity emailu, vygenerovanie dočasného hesla,
 *  uloženie používateľa s rolou zamestnanca a zostavenie odpovede pre frontend.
 */
@Service
public class EmployeeAccountServiceImpl implements EmployeeAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Vytvorí nový účet zamestnanca.
    @Override
    public EmployeeAccountResponse create(EmployeeCreateRequest user) {

        // Kontrola, či email už nie je zaregistrovaný.
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email je už zaregistrovaný.");
        }

        // Vygenerovanie dočasného hesla pre nový účet.
        String temporaryPassword = generateTemporaryPassword();

        // Nastavenie údajov používateľa.
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(temporaryPassword));
        userEntity.setRole(Role.EMPLOYEE);

        UserEntity savedUser;

        try {
            savedUser = userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Duplicitný email.");
        }

        // Pripravenie odpovede pre frontend.
        EmployeeAccountResponse response = new EmployeeAccountResponse();
        response.setUserId(savedUser.getUserId());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());
        response.setTemporaryPassword(temporaryPassword);
        response.setMessage("Zamestnanec bol úspešne vytvorený.");

        return response;
    }

    // Vygeneruje náhodné dočasné heslo pre nový zamestnanecký účet.
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



