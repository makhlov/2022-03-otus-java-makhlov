package ru.otus.atm.money;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableSet;


public enum Currency {
    RUB(convertToSet(100, 200, 500, 1000, 2000, 5000)),
    EUR(convertToSet(5, 10, 20, 50, 100, 200, 500));

    private static final Map<Currency, Set<BigInteger>> ACCEPTABLE_DENOMINATION_FOR_CURRENCY = new HashMap<>();;

    static {
        for (var currency : values()) {
            ACCEPTABLE_DENOMINATION_FOR_CURRENCY.put(currency, currency.denomination);
        }
    }

    final Set<BigInteger> denomination;
    Currency(Set<BigInteger> denomination) {
        this.denomination = unmodifiableSet(denomination);
    }

    private static Set<BigInteger> convertToSet(int... denomination) {
        return stream(denomination)
                .mapToObj(e -> new BigInteger(String.valueOf(e)))
                .collect(Collectors.toSet());
    }

    public Set<BigInteger> getAcceptableDenominations() {
        return this.denomination;
    }

    public static Set<BigInteger> getAcceptableDenominations(Currency currency) {
        return ACCEPTABLE_DENOMINATION_FOR_CURRENCY.get(currency);
    }
}
