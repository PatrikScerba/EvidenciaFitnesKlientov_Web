package sk.patrikscerba.gym.entity;

import jakarta.persistence.*;
import sk.patrikscerba.gym.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import sk.patrikscerba.gym.enums.SecurityQuestion;

/**
 * Entita reprezentujúca používateľa systému.
 * Uchováva prihlasovacie údaje, rolu, bezpečnostnú otázku,
 * hash bezpečnostnej odpovede a stav hesla používateľa v databáze.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "client_id")
    private  ClientEntity client;

    @Column(nullable = false)
    private boolean usingTemporaryPassword = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_question", nullable = false, length = 100)
    private SecurityQuestion securityQuestion;

    @Column(name = "security_answer_hash", nullable = false)
    private String securityAnswerHash;

    @Column(name = "password_change_required", nullable = false)
    private boolean passwordChangeRequired;

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswerHash() {
        return securityAnswerHash;
    }

    public void setSecurityAnswerHash(String securityAnswerHash) {
        this.securityAnswerHash = securityAnswerHash;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public boolean isUsingTemporaryPassword() {
        return usingTemporaryPassword;
    }

    public void setUsingTemporaryPassword(boolean usingTemporaryPassword) {
        this.usingTemporaryPassword = usingTemporaryPassword;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
