package sk.patrikscerba.gym.service.email;

public interface SystemEmailService {

    void sendRegistrationConfirmation(
            String email,
            String firstName,
            String lastName,
            String qrToken
    );

    void sendQrResetConfirmation(
            String email,
            String firstName,
            String lastName,
            String qrToken
    );


}
