package sk.patrikscerba.gym.exception;

import java.time.LocalDateTime;

// Model pre jednotnú chybovú odpoveď aplikácie.
public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;


    public ErrorResponse(String message, int status, LocalDateTime timestamp){
        this.message= message;
        this.status = status;
        this.timestamp = timestamp;

    }

    public String getMessage(){
        return  message;
    }
    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}




