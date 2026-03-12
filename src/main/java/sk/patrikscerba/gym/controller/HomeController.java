package sk.patrikscerba.gym.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Jednoduchý controller na otestovanie, že aplikácia beží
 * a endpoint je dostupný.
 */
@RestController
public class HomeController {

    // Základný testovací endpoint.
    @GetMapping("/")
    public String home() {
        return "Aplikácia beží úspešne";
    }
}