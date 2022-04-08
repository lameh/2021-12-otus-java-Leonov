package ru.otus;

import ru.otus.atm.ATM;
import ru.otus.atm.ATMImpl;
import ru.otus.atm.cell.Banknote;

import java.util.Map;

import static ru.otus.atm.cell.Banknote.*;

public class AtmAction {

    public static void main(String[] args) {
        final ATM atm = new ATMImpl(Banknote.values());

        System.out.println("ATM has zero balance, ("+ atm.getBalance() +") but we try to take some money");
        try{
            atm.getBanknotes(5000);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        final int depositSum = atm.addBanknotes(Map.of(
                HUNDRED, 100,
                FIVE_HUNDRED, 100,
                THOUSAND, 10,
                FIVE_THOUSAND, 10));
        System.out.println("Let's deposit " + depositSum);
        System.out.println("Now you have " + atm.getBalance());

        System.out.println("Let's try to take more than we have");
        try{
            atm.getBanknotes(400000);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("Let's try to take unsupported sum (150)");
        try{
            atm.getBanknotes(150);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println("The requested amount is issued in large denomination if possible");
            ATMImpl.getDenominationBanknotes(atm,12800);
            ATMImpl.getDenominationBanknotes(atm,21400);
            ATMImpl.getDenominationBanknotes(atm,8700);
        }


}
