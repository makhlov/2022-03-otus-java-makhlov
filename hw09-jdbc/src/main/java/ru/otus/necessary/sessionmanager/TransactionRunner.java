package ru.otus.necessary.sessionmanager;

public interface TransactionRunner {

    <T> T doInTransaction(TransactionAction<T> action);
}
