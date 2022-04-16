package ru.otus;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> customerDataMap;

    public CustomerService() {
        customerDataMap = new TreeMap<>(/*Comparator.comparingLong(Customer::getScores) - alternative*/);
    }

    public Map.Entry<Customer, String> getSmallest() {
        return Map.entry(new Customer(customerDataMap.firstEntry().getKey()), customerDataMap.firstEntry().getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = customerDataMap.higherEntry(customer);
        return higherEntry != null ? Map.entry(new Customer(higherEntry.getKey()), higherEntry.getValue()) : null;

        /*
           Map.Entry<Customer, String> nullableEntry
                = Optional.ofNullable(customerDataMap.higherEntry(customer)).orElseThrow(IllegalArgumentException::new);
           return Map.entry(new Customer(nullableEntry.getKey()), nullableEntry.getValue());
        */
    }

    public void add(Customer customer, String data) {
        customerDataMap.put(customer, data);
    }
}
