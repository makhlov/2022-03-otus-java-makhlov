package ru.otus.atm.money.factory;

import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

public abstract class BanknoteCreator {
    public abstract Banknote create(Currency currency, String denomination) throws IllegalArgumentException;
}
