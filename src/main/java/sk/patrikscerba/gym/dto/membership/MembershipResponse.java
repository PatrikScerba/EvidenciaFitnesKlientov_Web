package sk.patrikscerba.gym.dto.membership;

import sk.patrikscerba.gym.enums.MembershipStatus;

import java.time.LocalDate;

/**
 * DTO objekt pre odpoveď o permanentke.
 * Obsahuje základné informácie o permanentke klienta,
 * jej stave a dátume platnosti.
 */
public class MembershipResponse {

    private Long id;
    private Long clientId;
    private MembershipStatus status;
    private LocalDate validFrom;
    private LocalDate validTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}


