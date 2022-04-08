package ru.otus.atm.cell;

public interface Cell {

    int addBanknotes (int amount);

    int getBanknotes();

    int getCellDenomination();

    Banknote getBanknoteType();

    int getCellAmount();
}
