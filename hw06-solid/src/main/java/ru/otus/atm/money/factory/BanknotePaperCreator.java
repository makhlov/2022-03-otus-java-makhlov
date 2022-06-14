package ru.otus.atm.money.factory;

import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.BanknotePaper;
import ru.otus.atm.money.Currency;

import java.math.BigInteger;

public class BanknotePaperCreator extends BanknoteCreator {
    @Override
    public Banknote create(Currency currency, String denomination) throws IllegalArgumentException {
        return new BanknotePaper(new BigInteger(denomination), currency);
    }
}
