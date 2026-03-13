package sk.patrikscerba.gym.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.LoginRequest;
import sk.patrikscerba.gym.dto.LoginResponse;
import sk.patrikscerba.gym.service.AuthService;

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
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}