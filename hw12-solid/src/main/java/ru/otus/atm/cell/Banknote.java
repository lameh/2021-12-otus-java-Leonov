package ru.otus.atm.cell;

public enum Banknote {
    FIVE_THOUSAND(5000),
    THOUSAND(1000),
    FIVE_HUNDRED(500),
    HUNDRED(100);

    private final int denomination;

    Banknote(int denomination) {
        this.denomination = denomination;
    }

    public int getBanknoteDenomination() {
        return this.denomination;
    }
}
