package ru.otus.atm.mechanism.cluster.factory;

import ru.otus.atm.mechanism.cluster.ClusterManager;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

public abstract class ClusterCreator<T extends Banknote> {
    public abstract ClusterManager<T> create(Currency currency);
}
