package sk.patrikscerba.gym.dto.entry;

import sk.patrikscerba.gym.enums.EntryStatus;
import sk.patrikscerba.gym.enums.Reason;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO objekt, ktorý slúži na vrátenie údajov o zaznamenanom vstupe.
 * Obsahuje základné informácie o klientovi a stave jeho vstupu.
 */
public class EntryResponse {

    private Long id;
    private Long clientId;
    private String firstName;
    private String lastName;
    private EntryStatus status;
    private Reason reason;
    private LocalDate date;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}



