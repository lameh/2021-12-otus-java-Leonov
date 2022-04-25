package ru.otus.atm.cell;

public class CellImpl implements Cell {

    private final Banknote banknote;
    private int cellAmount = 0;

    public CellImpl(Banknote banknote) {
        this.banknote = banknote;
    }

    @Override
    public int addBanknotes(int amount) {
        this.cellAmount += amount;
        return amount * banknote.getBanknoteDenomination();
    }

    @Override
    public int getBanknotes() {
        this.cellAmount--;
        return banknote.getBanknoteDenomination();
    }

    @Override
    public int getCellDenomination() {
        return banknote.getBanknoteDenomination();
    }

    @Override
    public Banknote getBanknoteType() {
        return banknote;
    }

    @Override
    public int getCellAmount() {
        return cellAmount;
    }
}
