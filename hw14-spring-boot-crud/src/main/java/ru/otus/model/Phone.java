package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phone")
public class Phone {

    @Id
    private final Long clientId;

    private final String number;

    public Phone(String number) {
        this(null, number);
    }

    @PersistenceCreator
    public Phone(Long clientId, String number) {
        this.clientId = clientId;
        this.number = number;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getNumber() {
        return number;
    }
}
