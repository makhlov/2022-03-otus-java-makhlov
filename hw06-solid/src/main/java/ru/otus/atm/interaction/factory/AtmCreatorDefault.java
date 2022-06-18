package ru.otus.atm.interaction.factory;

import ru.otus.atm.interaction.Atm;
import ru.otus.atm.interaction.AtmDefault;
import ru.otus.atm.mechanism.cell.factory.CellBanknoteCreator;
import ru.otus.atm.mechanism.cluster.ClusterManager;
import ru.otus.atm.mechanism.cluster.factory.ClusterCellBanknoteCreator;
import ru.otus.atm.mechanism.cluster.factory.ClusterCreator;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

import java.util.HashMap;
import java.util.List;

public class AtmCreatorDefault extends AtmCreator {
    private final ClusterCreator<Banknote> clusterCreator;
    private final List<Currency> currencyList;

    public AtmCreatorDefault(List<Currency> currencyList) {
        this.clusterCreator = new ClusterCellBanknoteCreator(new CellBanknoteCreator());
        this.currencyList = currencyList;
    }

    @Override
    public Atm create() {
        HashMap<Currency, ClusterManager<Banknote>> atmCurrencyClusters = new HashMap<>();
        for (var currency:currencyList) {
            atmCurrencyClusters.put(currency, clusterCreator.create(currency));
        }
        return new AtmDefault(atmCurrencyClusters);
    }
}
