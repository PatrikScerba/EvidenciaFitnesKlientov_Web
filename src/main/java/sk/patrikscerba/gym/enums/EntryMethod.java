package sk.patrikscerba.gym.enums;

/**
 * Enum reprezentujúci spôsob, akým bol vykonaný vstup alebo odchod klienta.
 * <p>
 * MANUAL    - ručné zaznamenanie pracovníkom fitnescentra bez použitia QR kódu.
 * QR_MANUAL - náhradné alebo servisné spracovanie QR kódu, napríklad pri výpadku turniketu.
 * QR_SCAN   - automatické spracovanie po naskenovaní QR kódu cez bežný scan.
 */
public enum EntryMethod {

    MANUAL,
    QR_MANUAL,
    QR_SCAN
}
