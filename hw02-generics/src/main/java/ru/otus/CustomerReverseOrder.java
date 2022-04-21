package ru.otus;

import java.util.Deque;
import java.util.LinkedList;

/* Необходимо реализовать методы класса, подобрав правильную DS */
public class CustomerReverseOrder {
    private final Deque<Customer> customerDeque;

    public CustomerReverseOrder() {
        customerDeque = new LinkedList<>();
    }

    public void add(Customer customer) {
        customerDeque.push(customer);
    }

    public Customer take() {
        return customerDeque.pop();
    }
}