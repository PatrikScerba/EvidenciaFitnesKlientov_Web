package sk.patrikscerba.gym.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import sk.patrikscerba.gym.dto.auth.LoginRequest;
import sk.patrikscerba.gym.dto.auth.LoginResponse;

/**
 * Servis pre autentifikáciu používateľa.
 * Definuje operáciu prihlásenia do systému.
 */
public interface AuthService {

    // Metóda pre overenie prihlasovacích údajov používateľa
    // Pri úspechu vráti odpoveď s informáciami o prihlásenom používateľovi
    LoginResponse login(LoginRequest request, HttpServletRequest httpRequest);

    // Vráti údaje o aktuálne prihlásenom používateľovi podľa emailu.
    LoginResponse getCurrentUser(String email);
}
