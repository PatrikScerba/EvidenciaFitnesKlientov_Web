package sk.patrikscerba.gym.dto.auth;

import sk.patrikscerba.gym.enums.SecurityQuestion;

/**
 * DTO odpoveď obsahujúca bezpečnostnú otázku používateľa podľa zadaného emailu.
 */
public class SecurityQuestionResponse {

    private String email;
    private SecurityQuestion securityQuestion;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}

