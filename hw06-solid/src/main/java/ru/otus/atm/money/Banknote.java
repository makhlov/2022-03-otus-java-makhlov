package ru.otus.atm.money;

import java.math.BigInteger;

public interface Banknote {
    BigInteger getDenomination();
    Currency getCurrency();
}
