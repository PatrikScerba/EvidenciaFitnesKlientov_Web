package sk.patrikscerba.gym.enums;

/**
 * Dôvod výsledku pri pokuse o vstup klienta.
 */
public enum Reason {

    OK,
    ALREADY_ENTERED_TODAY,
    NO_MEMBERSHIP,
    MEMBERSHIP_EXPIRED,
    AGE_RESTRICTION,
    ACTIVE_ENTRY_ALREADY_EXISTS
}
