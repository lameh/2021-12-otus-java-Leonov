package ru.otus.atm;

import ru.otus.atm.cell.Banknote;
import ru.otus.atm.cell.Cell;
import ru.otus.atm.cell.CellImpl;

import java.util.*;

public class ATMImpl implements ATM {
    private final MoneyStorage atmStorage = new MoneyStorage();

    public ATMImpl(Banknote[] banknotes) {
        for (int i = 0; i < banknotes.length; i++) {
            this.atmStorage.getMoneyStorage().add(new CellImpl(banknotes[i]));
        }
    }

    @Override
    public Map<Banknote, Integer> getBanknotes(int amount) {
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Requested amount " + amount + " is greater than you have " + getBalance());
        }
        if (amount % Banknote.HUNDRED.getBanknoteDenomination() != 0) {
            throw new IllegalArgumentException("Requested amount " + amount + " could not be issued. Enter the amount in multiples of 100");
        }
        return getInCell(amount);
    }

    @Override
    public int addBanknotes(Map<Banknote, Integer> banknotes) {
        final int sum = addInCell(banknotes);
        return sum;
    }

    @Override
    public int getBalance() {
        int amountBalance = 0;
        for(Cell cell : atmStorage.getMoneyStorage()) {
            amountBalance += cell.getCellAmount() * cell.getCellDenomination();
        }
        return amountBalance;
    }

    private int addInCell (Map<Banknote, Integer> banknotes) {
        int result = 0;

        for (Cell cell : atmStorage.getMoneyStorage()) {
            if (banknotes.containsKey(cell.getBanknoteType())) {
                result += cell.addBanknotes(banknotes.get(cell.getBanknoteType()));
            } else {
                continue;
            }
        }
        return result;
    }

    private Map<Banknote, Integer> getInCell (int sum) {
        final Map<Banknote, Integer> result = new HashMap<>();

        for (Cell cell : atmStorage.getMoneyStorage()) {
            int number = 0;
            Banknote banknote = cell.getBanknoteType();
                while (cell.getCellAmount() > number && sum >= banknote.getBanknoteDenomination()) {
                    sum -= cell.getBanknotes();
                    number++;
                }
            result.put(banknote, number);
        }
        return result;
    }

    public static void getDenominationBanknotes(ATM atm, int amount) {
        final Map<Banknote, Integer> banknotes = atm.getBanknotes(amount);
        System.out.print("Issued: " + amount + " -> ");
        for (var banknote : banknotes.entrySet()) {
            System.out.print(banknote.toString() + " ");
        }
        System.out.println(" \nBalance = " + atm.getBalance());

    }
}
