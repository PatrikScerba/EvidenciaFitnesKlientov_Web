package sk.patrikscerba.gym.service;

import sk.patrikscerba.gym.dto.LoginRequest;
import sk.patrikscerba.gym.dto.LoginResponse;

/**
 * Servis pre autentifikáciu používateľa.
 * Definuje operáciu prihlásenia do systému.
 */
public interface AuthService {

    // Metóda pre overenie prihlasovacích údajov používateľa
    // Pri úspechu vráti odpoveď s informáciami o prihlásenom používateľovi
    LoginResponse login(LoginRequest request);
}
