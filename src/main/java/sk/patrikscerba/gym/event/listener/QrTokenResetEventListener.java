package sk.patrikscerba.gym.event.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import sk.patrikscerba.gym.event.QrTokenResetEvent;
import sk.patrikscerba.gym.service.email.SystemEmailService;

@Component
public class QrTokenResetEventListener {

    private final SystemEmailService systemEmailService;

    public QrTokenResetEventListener(SystemEmailService systemEmailService) {
        this.systemEmailService = systemEmailService;
    }

    // Spracuje udalosť až po úspešnom potvrdení databázovej transakcie.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleQrTokenReset(QrTokenResetEvent event) {

        systemEmailService.sendQrResetConfirmation(
                event.email(),
                event.firstName(),
                event.lastName(),
                event.qrToken()
        );
    }
}

