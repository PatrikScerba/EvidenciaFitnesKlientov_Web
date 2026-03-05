package sk.patrikscerba.gym.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "klienti")
public class KlientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long klientId;

    @Column(nullable = false)
    private String krstneMeno;

    @Column(nullable = false)
    private String priezvisko;

    @Column(nullable = false)
    private String telefonneCislo;

    @Column(nullable = false)
    private LocalDate datumNarodenia;

    @Column(nullable = false)
    private String adresa;

    @Column(nullable = false)
    private String email;

    public Long getKlientId() {
        return klientId;
    }

    public void setKlientId(Long klientId) {
        this.klientId = klientId;
    }

    public String getKrstneMeno() {
        return krstneMeno;
    }

    public void setKrstneMeno(String krstneMeno) {
        this.krstneMeno = krstneMeno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }

    public String getTelefonneCislo() {
        return telefonneCislo;
    }

    public void setTelefonneCislo(String telefonneCislo) {
        this.telefonneCislo = telefonneCislo;
    }

    public LocalDate getDatumNarodenia() {
        return datumNarodenia;
    }

    public void setDatumNarodenia(LocalDate datumNarodenia) {
        this.datumNarodenia = datumNarodenia;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
