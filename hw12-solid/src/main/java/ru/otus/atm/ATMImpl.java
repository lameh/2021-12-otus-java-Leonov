package ru.otus.atm;

import ru.otus.atm.cell.Banknote;
import ru.otus.atm.cell.Cell;
import ru.otus.atm.cell.CellImpl;

import java.util.*;

public class ATMImpl implements ATM {
    private final List<Cell> atmStorage = new ArrayList<>();
    private int amountBalance = 0;

    public ATMImpl(Banknote[] banknotes) {
        for (int i = 0; i < banknotes.length; i++) {
            this.atmStorage.add(new CellImpl(banknotes[i]));
        }
    }

    @Override
    public Map<Banknote, Integer> getBanknotes(int amount) {
        if (amount > amountBalance) {
            throw new IllegalArgumentException("Requested amount " + amount + " is greater than you have " + amountBalance);
        }
        if (amount % 100 != 0) {
            throw new IllegalArgumentException("Requested amount " + amount + " could not be issued. Enter the amount in multiples of 100");
        }
        amountBalance -= amount;
        return getInCell(amount);
    }

    @Override
    public int addBanknotes(Map<Banknote, Integer> banknotes) {
        final int sum = addInCell(banknotes);
        amountBalance += sum;
        return sum;
    }

    @Override
    public int getBalance() {
        return amountBalance;
    }

    private int addInCell (Map<Banknote, Integer> banknotes) {
        int result = 0;

        for (Cell cell : atmStorage) {
            if (banknotes.containsKey(cell.getBanknoteType())) {
                result += cell.addBanknotes(banknotes.get(cell.getBanknoteType()));
            }
        }
        return result;
    }

    private Map<Banknote, Integer> getInCell (int sum) {
        final Map<Banknote, Integer> result = new HashMap<>();

        for (Cell cell : atmStorage) {
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
