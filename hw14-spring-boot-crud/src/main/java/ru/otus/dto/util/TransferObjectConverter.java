package ru.otus.dto.util;

import ru.otus.dto.ClientTransferObject;
import ru.otus.model.Client;

public interface TransferObjectConverter {
    Client toClient(ClientTransferObject clientTransferObject);
    ClientTransferObject toTransferObject(Client client);
}
