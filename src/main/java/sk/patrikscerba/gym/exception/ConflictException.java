package sk.patrikscerba.gym.exception;

// Výnimka pre konfliktné stavy, napríklad duplicitu údajov.
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
