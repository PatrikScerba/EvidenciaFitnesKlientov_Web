package sk.patrikscerba.gym.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sk.patrikscerba.gym.dto.membership.MembershipCreateRequest;
import sk.patrikscerba.gym.dto.membership.MembershipResponse;
import sk.patrikscerba.gym.service.membership.MembershipService;

/**
 * Controller pre správu permanentiek.
 * Zabezpečuje vytvorenie alebo predĺženie permanentky,
 * získanie permanentky podľa ID klienta
 * a načítanie permanentky aktuálne prihláseného klienta.
 */
@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    // Endpoint na vytvorenie alebo obnovenie permanentky klienta.
    @PostMapping
    public ResponseEntity<MembershipResponse> createOrExtendMembership(
            @Valid @RequestBody MembershipCreateRequest request) {
        MembershipResponse response = membershipService.createOrExtendMembership(request);

        // Vrátenie odpovede s HTTP statusom 201 Created
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint na získanie permanentky podľa ID klienta.
    @GetMapping("/client/{clientId}")
    public ResponseEntity<MembershipResponse> getMembershipByClientId(@PathVariable Long clientId) {
        MembershipResponse response = membershipService.getMembershipByClientId(clientId);
        return ResponseEntity.ok(response);
    }

    // Endpoint na získanie permanentky aktuálne prihláseného klienta.
    @GetMapping("/me")
    public ResponseEntity<MembershipResponse> getMyMembership(Authentication authentication) {
        String email = authentication.getName();
        MembershipResponse response = membershipService.getCurrentClientMembership(email);
        return ResponseEntity.ok(response);
    }
}

