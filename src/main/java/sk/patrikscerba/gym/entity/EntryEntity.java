package sk.patrikscerba.gym.entity;

import jakarta.persistence.*;
import sk.patrikscerba.gym.enums.EntryMethod;
import sk.patrikscerba.gym.enums.EntryStatus;
import sk.patrikscerba.gym.enums.Reason;

import java.time.LocalDateTime;

/**
 * Entita reprezentujúca záznam o vstupe klienta.
 * Obsahuje čas príchodu, odchodu, stav vstupu,
 * dôvod rozhodnutia, poznámku a spôsob, akým bol vstup alebo odchod vykonaný.
 */
@Entity
@Table(name = "entries")
public class EntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Reason reason;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "note", length = 255)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_method")
    private EntryMethod arrivalMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "departure_method")
    private EntryMethod departureMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public EntryStatus getStatus() {
        return status;
    }

    public void setStatus(EntryStatus status) {
        this.status = status;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EntryMethod getArrivalMethod() {
        return arrivalMethod;
    }

    public void setArrivalMethod(EntryMethod arrivalMethod) {
        this.arrivalMethod = arrivalMethod;
    }

    public EntryMethod getDepartureMethod() {
        return departureMethod;
    }

    public void setDepartureMethod(EntryMethod departureMethod) {
        this.departureMethod = departureMethod;
    }
}
