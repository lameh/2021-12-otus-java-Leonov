package ru.otus.atm;

import ru.otus.atm.cell.Cell;
import ru.otus.atm.cell.CellImpl;

import java.util.ArrayList;
import java.util.List;

public class MoneyStorage {

    private List<Cell> moneyStorage = new ArrayList<>();

    public List<Cell> getMoneyStorage() {
        return this.moneyStorage;
    }
}
