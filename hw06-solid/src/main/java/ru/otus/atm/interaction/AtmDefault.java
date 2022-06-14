package ru.otus.atm.interaction;

import ru.otus.atm.interaction.exception.AtmInteractionException;
import ru.otus.atm.mechanism.cluster.ClusterManager;
import ru.otus.atm.mechanism.cluster.exception.ClusterOperationException;
import ru.otus.atm.money.Banknote;
import ru.otus.atm.money.Currency;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.function.Function;

import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class AtmDefault implements Atm {
    private final HashMap<Currency, ClusterManager<Banknote>> atmCurrencyClusters;

    private record RequestedBanknote(BigInteger denomination, int count) { }

    public AtmDefault(HashMap<Currency, ClusterManager<Banknote>> atmCurrencyClusters) {
        this.atmCurrencyClusters = atmCurrencyClusters;
    }

    @Override
    public List<Banknote> requestMoney(Currency currency, String amount) throws AtmInteractionException {
        List<BigInteger> denominations = splitRequestedIntoBanknotes(currency, amount);
        List<RequestedBanknote> requestedBanknoteByCount = groupBanknotes(denominations);

        var cluster = atmCurrencyClusters.get(currency);
        long flags = 0;
        for (var requested : requestedBanknoteByCount) {
            flags += checkBanknotesSufficiency(cluster, requested) ? 1 : 0;
        }

        List<Banknote> out = new ArrayList<>();
        if (flags == requestedBanknoteByCount.size()) {
            for (var requested : requestedBanknoteByCount) {
                try {
                    out.addAll(cluster.requestBanknote(requested.denomination, requested.count));
                } catch (ClusterOperationException e) {
                    throw new AtmInteractionException("Error receiving banknotes from the cluster", e);
                }
            }
        } else {
            throw new AtmInteractionException("There is not enough banknotes in the ATM");
        }
        return out;
    }

    private boolean checkBanknotesSufficiency(ClusterManager<Banknote> cluster, RequestedBanknote banknoteRecord) {
        return cluster.getBanknoteAmountFromCell(banknoteRecord.denomination) >= banknoteRecord.count;
    }

    private static List<RequestedBanknote> groupBanknotes(List<BigInteger> banknotes) {
        Map<BigInteger, Long> frequencyMap = banknotes.stream()
                .collect(groupingBy(Function.identity(),
                        counting()));

        List<RequestedBanknote> result = new ArrayList<>();
        for (var entry: frequencyMap.entrySet()) {
            result.add(new RequestedBanknote(entry.getKey(), toIntExact(entry.getValue())));
        }
        return result;
    }

    private static List<BigInteger> splitRequestedIntoBanknotes(Currency currency, String amount) {
        List<BigInteger> requested = new LinkedList<>();
        BigInteger amountB = new BigInteger(amount);
        while (amountB.compareTo(BigInteger.ZERO) > 0) {
            BigInteger nearest = findNearest(currency, amountB);
            requested.add(nearest);
            amountB = amountB.subtract(nearest);
        }
        return requested;
    }

    private static BigInteger findNearest(Currency currency, BigInteger digitNumber) {
        NavigableSet<BigInteger> set = new TreeSet<>(currency.getAcceptableDenominations());
        return set.floor(digitNumber);
    }

    @Override
    public void insertMoney(List<Banknote> banknotes) {
        if (oneCurrencyCheck(banknotes)) {
            ClusterManager<Banknote> banknoteClusterManager = atmCurrencyClusters.get(banknotes.get(0).getCurrency());
            try {
                banknoteClusterManager.putBanknote(banknotes);
            } catch (ClusterOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Banknotes of different denominations!");
        }
    }

    private boolean oneCurrencyCheck(List<Banknote> banknotes) {
        Currency currency = banknotes.get(0).getCurrency();
        for (var banknote : banknotes) {
            if (banknote.getCurrency() != currency) return false;
        }
        return true;
    }

    @Override
    public BigInteger getBalanceForCurrency(Currency currency) {
        return atmCurrencyClusters.get(currency).getTotalSumFromCluster();
    }
}