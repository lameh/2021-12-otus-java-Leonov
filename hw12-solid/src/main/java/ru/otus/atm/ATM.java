package ru.otus.atm;

import ru.otus.atm.cell.Banknote;

import java.util.Map;

public interface ATM {

    Map<Banknote, Integer> getBanknotes(int amount);

    int addBanknotes(Map<Banknote, Integer> banknotes);

    int getBalance();
}
