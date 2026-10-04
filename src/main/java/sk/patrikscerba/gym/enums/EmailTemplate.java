package sk.patrikscerba.gym.enums;

/**
 * Definuje HTML šablóny používané pri odosielaní e-mailov.
 */
public enum EmailTemplate {

    NOTIFICATION("email/notification-email"),
    REGISTRATION("email/system-email"),
    QR_RESET("email/qr-reset-email");

    private final String templatePath;

    EmailTemplate(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}