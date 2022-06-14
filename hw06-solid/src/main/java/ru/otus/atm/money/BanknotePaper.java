package ru.otus.atm.money;

import java.math.BigInteger;

public record BanknotePaper(BigInteger denomination, Currency currency) implements Banknote {

    public BanknotePaper {
        if (!currency.getAcceptableDenominations().contains(denomination))
            throw new IllegalArgumentException();
    }

    @Override
    public BigInteger getDenomination() {
        return denomination;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }
}
