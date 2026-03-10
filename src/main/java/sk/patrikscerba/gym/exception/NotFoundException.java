package sk.patrikscerba.gym.exception;

// Výnimka pre situácie, keď požadovaný záznam nebol nájdený.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
