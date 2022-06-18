package ru.otus.atm.mechanism.cell.factory;

import ru.otus.atm.mechanism.cell.Cell;
import ru.otus.atm.mechanism.cell.CellBanknote;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

public class CellBanknoteCreator extends CellCreator<Banknote> {
    @Override
    public Cell<Banknote> create(Integer denomination, Currency currency) {
        return new CellBanknote(denomination, currency, DEFAULT_CELL_SIZE);
    }
}
