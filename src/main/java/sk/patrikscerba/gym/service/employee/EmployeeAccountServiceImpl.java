package sk.patrikscerba.gym.service.employee;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.employee.EmployeeAccountResponse;
import sk.patrikscerba.gym.dto.employee.EmployeeCreateRequest;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.enums.Role;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.ConflictException;
import sk.patrikscerba.gym.repository.UserRepository;

import java.security.SecureRandom;

/**
 * Implementácia servisnej vrstvy pre vytvorenie účtu zamestnanca.
 * Trieda zabezpečuje kontrolu duplicity emailu, vygenerovanie dočasného hesla,
 * uloženie používateľa s rolou zamestnanca, uloženie bezpečnostnej otázky, odpovede
 * a informáciu o používaní dočasného hesla s možnosťou jeho zmeny.
 */
@Service
public class EmployeeAccountServiceImpl implements EmployeeAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Vytvorí nový účet zamestnanca.
    @Override
    @Transactional
    public EmployeeAccountResponse create(EmployeeCreateRequest request) {

        // Kontrola, či email už nie je zaregistrovaný.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email je už zaregistrovaný.");
        }

        String securityAnswer = request.getSecurityAnswer().trim().toLowerCase();
        String confirmSecurityAnswer = request.getConfirmSecurityAnswer().trim().toLowerCase();

        if (!securityAnswer.equals(confirmSecurityAnswer)) {
            throw new BusinessException("Bezpečnostné odpovede sa nezhodujú.");
        }

        // Vygenerovanie dočasného hesla pre nový účet.
        String temporaryPassword = generateTemporaryPassword();

        // Nastavenie údajov používateľa.
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(temporaryPassword));
        userEntity.setUsingTemporaryPassword(true);
        userEntity.setPasswordChangeRequired(true);
        userEntity.setRole(Role.EMPLOYEE);
        userEntity.setSecurityQuestion(request.getSecurityQuestion());
        userEntity.setSecurityAnswerHash(passwordEncoder.encode(securityAnswer));

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



