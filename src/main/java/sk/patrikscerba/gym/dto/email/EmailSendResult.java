package sk.patrikscerba.gym.dto.email;

public record EmailSendResult(
        int total,
        int successful,
        int failed
) {
}