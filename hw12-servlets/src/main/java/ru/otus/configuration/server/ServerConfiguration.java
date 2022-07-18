package ru.otus.configuration.server;

import ru.otus.service.web.server.ClientWebServer;

import java.io.IOException;

public interface ServerConfiguration {
    ClientWebServer configure() throws IOException;
}
