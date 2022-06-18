package ru.otus.atm.interaction;

import ru.otus.atm.interaction.exception.AtmInteractionException;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

import java.util.List;

public interface Atm {
    List<Banknote> requestMoney(Currency currency, String amount) throws AtmInteractionException;
    void insertMoney(List<Banknote> banknotes);
    Integer getBalanceForCurrency(Currency currency);
}