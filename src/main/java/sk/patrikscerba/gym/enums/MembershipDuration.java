package sk.patrikscerba.gym.enums;

/**
 * Enum určujúci dostupné dĺžky trvania permanentky v dňoch.
 */
public enum MembershipDuration {

    DAYS_30(30),
    DAYS_90(90),
    DAYS_180(180),
    DAYS_365(365);

    private final int days;

    MembershipDuration(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}

