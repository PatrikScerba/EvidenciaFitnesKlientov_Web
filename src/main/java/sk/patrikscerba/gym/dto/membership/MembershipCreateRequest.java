package sk.patrikscerba.gym.dto.membership;

import jakarta.validation.constraints.NotNull;
import sk.patrikscerba.gym.enums.MembershipDuration;

/**
 * DTO objekt pre vytvorenie alebo predĺženie permanentky.
 * Obsahuje ID klienta a zvolenú dĺžku trvania permanentky.
 */
public class MembershipCreateRequest {
    @NotNull(message = "ID klienta je povinné.")
    private Long clientId;

    @NotNull(message = "Dĺžka permanentky je povinná.")
    private MembershipDuration duration;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public MembershipDuration getDuration() {
        return duration;
    }

    public void setDuration(MembershipDuration duration) {
        this.duration = duration;
    }
}

