package ru.otus.service.web.helper;

import jakarta.servlet.http.HttpServletRequest;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ClientMappingHelper {
    public Map<String, Object> mapToTemplateData(Client client) {
        if (isNull(client)) {
            return null;
        } else {
            return Map.of("id", client.getId(),
                    "name", client.getName(),
                    "address", nonNull(client.getAddress()) ? client.getAddress().getStreet() : "",
                    "phones", isNull(client.getPhones()) ? "" :
                                client.getPhones()
                                    .stream()
                                    .map(Phone::getNumber)
                                    .collect(joining(",")));
        }
    }

    public Client mapToInstance(HttpServletRequest request) {
        var instance = new Client();

        instance.setName(request.getParameter("name"));
        var address = request.getParameter("address");
        instance.setAddress(nonNull(address) ? new Address(null, address) : null);
        var phones = request.getParameter("phone");
        if (nonNull(phones)) {
            instance.setPhones(
                    stream(phones.split(","))
                    .map(phone -> new Phone(null, phone, instance))
                    .collect(toList()));
        }
        return instance;
    }
}
