package ru.otus.persistance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.persistance.repository.ClientRepository;
import ru.otus.persistance.sessionmanager.TransactionManager;

import java.util.List;

@Service
public class DBServiceClientImpl implements DBServiceClient{

    private static final Logger log = LoggerFactory.getLogger(DBServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public DBServiceClientImpl(ClientRepository clientRepository, TransactionManager transactionManager) {
        this.clientRepository = clientRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(() -> {
            Client savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }
}
