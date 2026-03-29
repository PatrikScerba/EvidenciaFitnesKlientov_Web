package sk.patrikscerba.gym.service.auth;

import sk.patrikscerba.gym.dto.auth.ResetPasswordRequest;
import sk.patrikscerba.gym.dto.auth.ResetPasswordResponse;
import sk.patrikscerba.gym.dto.auth.SecurityQuestionResponse;

/**
 * Servisné rozhranie pre reset hesla používateľa pomocou bezpečnostnej otázky a odpovede.
 * Definuje získanie bezpečnostnej otázky podľa emailu a spracovanie resetu hesla.
 */
public interface PasswordResetService {

    SecurityQuestionResponse getSecurityQuestionByEmail(String email);

    ResetPasswordResponse resetPasswordBySecurityAnswer(ResetPasswordRequest request);
}

