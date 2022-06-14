package ru.otus.atm.mechanism.cluster;

import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;
import ru.otus.atm.mechanism.cluster.exception.ClusterOperationException;

import java.math.BigInteger;
import java.util.List;

public interface ClusterManager<T extends Banknote> {
    void putBanknote(List<Banknote> banknotes) throws ClusterOperationException;
    List<T> requestBanknote(BigInteger nominal, int amount) throws ClusterOperationException;
    int getBanknoteAmountFromCell(BigInteger denomination);
    BigInteger getTotalSumFromCluster();
    Currency getClusterCurrency();
}
