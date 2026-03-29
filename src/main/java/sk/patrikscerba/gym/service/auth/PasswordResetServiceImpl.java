package sk.patrikscerba.gym.service.auth;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.auth.ResetPasswordRequest;
import sk.patrikscerba.gym.dto.auth.ResetPasswordResponse;
import sk.patrikscerba.gym.dto.auth.SecurityQuestionResponse;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.UserRepository;

import java.security.SecureRandom;

/**
 * Implementácia servisnej vrstvy pre reset hesla používateľa pomocou bezpečnostnej otázky a odpovede.
 * Trieda zabezpečuje získanie bezpečnostnej otázky podľa emailu, overenie odpovede,
 * vytvorenie nového dočasného hesla a aktualizáciu používateľského účtu.
 */
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SecurityQuestionResponse getSecurityQuestionByEmail(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Používateľ s týmto emailom neexistuje."));

        SecurityQuestionResponse response = new SecurityQuestionResponse();
        response.setEmail(user.getEmail());
        response.setSecurityQuestion(user.getSecurityQuestion());

        return response;
    }

    // Resetuje heslo používateľa po správnom overení bezpečnostnej odpovede.
    @Override
    @Transactional
    public ResetPasswordResponse resetPasswordBySecurityAnswer(ResetPasswordRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Používateľ s týmto emailom neexistuje."));

        String securityAnswer = request.getSecurityAnswer().trim().toLowerCase();

        if (!passwordEncoder.matches(securityAnswer, user.getSecurityAnswerHash())) {
            throw new BusinessException("Bezpečnostná odpoveď nie je správna.");
        }

        String temporaryPassword = generateTemporaryPassword();

        user.setPassword(passwordEncoder.encode(temporaryPassword));
        user.setUsingTemporaryPassword(true);
        user.setPasswordChangeRequired(true);

        userRepository.save(user);

        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setEmail(user.getEmail());
        response.setTemporaryPassword(temporaryPassword);
        response.setMessage("Heslo bolo úspešne resetované.");

        return response;
    }

    // Vygeneruje nové dočasné heslo pri resete účtu.
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

















