package ru.otus.atm.mechanism.cluster.factory;

import ru.otus.atm.mechanism.cell.Cell;
import ru.otus.atm.mechanism.cell.factory.CellCreator;
import ru.otus.atm.mechanism.cluster.ClusterManager;
import ru.otus.atm.mechanism.cluster.ClusterManagerImpl;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ClusterCellBanknoteCreator extends ClusterCreator<Banknote> {
    private final CellCreator<Banknote> cellCreator;

    public ClusterCellBanknoteCreator(CellCreator<Banknote> cellCreator) {
        this.cellCreator = cellCreator;
    }

    @Override
    public ClusterManager<Banknote> create(Currency currency) {
        return new ClusterManagerImpl(currency, initCellHolder(currency));
    }

    private Map<Integer, Cell<Banknote>> initCellHolder(Currency currency) {
        return currency.getAcceptableDenominations().stream()
                .collect(toMap(e -> e, e -> cellCreator.create(e, currency)));
    }
}
