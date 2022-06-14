package ru.otus.atm.mechanism.cell;

import ru.otus.atm.money.Banknote;
import ru.otus.atm.mechanism.cell.exception.CellOperationException;

import java.math.BigInteger;

public interface Cell<T extends Banknote> {
    T extract() throws CellOperationException;
    void insertBanknote(T banknote) throws CellOperationException;
    int getBanknotesAmount();

    BigInteger getDenomination();
}