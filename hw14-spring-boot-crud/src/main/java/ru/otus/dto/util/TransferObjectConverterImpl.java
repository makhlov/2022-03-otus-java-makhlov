package ru.otus.dto.util;

import org.springframework.stereotype.Service;
import ru.otus.dto.ClientTransferObject;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Arrays;
import java.util.Set;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

@Service
public class TransferObjectConverterImpl implements TransferObjectConverter {

    @Override
    public Client toClient(ClientTransferObject clientTransferObject) {
        var phonesStr = clientTransferObject.getPhones();
        Set<Phone> phones = null;
        if (nonNull(phonesStr) && !phonesStr.isBlank()) {
            phones = Arrays.stream(phonesStr.split(","))
                           .map(Phone::new)
                           .collect(toSet());
        }
        return new Client(
                clientTransferObject.getId(),
                clientTransferObject.getName(),
                new Address(null, clientTransferObject.getAddress()),
                phones
        );
    }

    @Override
    public ClientTransferObject toTransferObject(Client client) {
        ClientTransferObject clientTransferObject = new ClientTransferObject();

        clientTransferObject.setId(client.getId());
        clientTransferObject.setName(client.getName());

        if (nonNull(client.getAddress())) {
            clientTransferObject.setAddress(
                    client.getAddress().getStreet()
            );
        }

        if (nonNull(client.getPhones())) {
            clientTransferObject.setPhones(
                    client.getPhones().stream().map(Phone::getNumber).collect(joining(","))
            );
        }
        return clientTransferObject;
    }

}
