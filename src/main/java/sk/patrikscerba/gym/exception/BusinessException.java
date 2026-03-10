package sk.patrikscerba.gym.exception;

// Výnimka pre porušenie business pravidiel v aplikácii.
public class BusinessException extends RuntimeException {

    public BusinessException(String message){
        super(message);
    }
}
