package sk.patrikscerba.gym.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.auth.LoginRequest;
import sk.patrikscerba.gym.dto.auth.LoginResponse;
import sk.patrikscerba.gym.service.auth.AuthService;

/**
 * Controller zodpovedný za autentifikáciu používateľa.
 * Obsahuje endpoint pre prihlásenie do systému.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Servis, ktorý obsahuje logiku prihlásenia používateľa
    private final AuthService authService;

    // Endpoint pre prihlásenie používateľa.
    // Zavolá servisnú vrstvu, ktorá overí údaje a vráti odpoveď.
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authService.login(request, httpRequest);
    }

    // Endpoint pre získanie údajov o aktuálne prihlásenom používateľovi.
    // Z autentifikácie získa email používateľa a cez servis vráti jeho údaje.
    @GetMapping("/me")
    public LoginResponse getCurrentUser(Authentication authentication) {
        return authService.getCurrentUser(authentication.getName());
    }
}

