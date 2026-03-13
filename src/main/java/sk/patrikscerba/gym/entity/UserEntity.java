package sk.patrikscerba.gym.entity;

import jakarta.persistence.*;
import sk.patrikscerba.gym.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Entita reprezentujúca používateľa systému.
 * Uchováva prihlasovacie údaje a rolu používateľa v databáze.
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
