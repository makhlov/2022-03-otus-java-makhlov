package ru.otus;

import java.util.Objects;

/*  Исправить ошибки в классе */
public class Customer implements Comparable<Customer>, Cloneable {
    private final long id;
    private String name;
    private long scores;

    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    /**
     * Copy constructor
     */
    public Customer(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getScores());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    @Override
    public int compareTo(Customer customer) {
        return Long.compare(scores, customer.getScores());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scores=" + scores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getId() == customer.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
