package ru.otus.persistance.service;

import ru.otus.model.Client;

import java.util.List;

public interface DBServiceClient {
    List<Client> findAll();
    Client save(Client client);
}
