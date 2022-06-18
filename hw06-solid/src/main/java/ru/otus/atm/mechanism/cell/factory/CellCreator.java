package ru.otus.atm.mechanism.cell.factory;

import ru.otus.atm.money.Banknote;
import ru.otus.atm.mechanism.cell.Cell;
import ru.otus.atm.money.Currency;

public abstract class CellCreator<T extends Banknote> {
    protected static final int DEFAULT_CELL_SIZE = 2500;
    abstract public Cell<T> create(Integer denomination, Currency currency);
}