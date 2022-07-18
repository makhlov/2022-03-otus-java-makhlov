package ru.otus.configuration.server;

import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import ru.otus.persistance.dao.UserDao;
import ru.otus.service.db.InMemoryLoginServiceImpl;
import ru.otus.service.db.DBServiceClient;
import ru.otus.persistance.dao.InMemoryUserDao;
import ru.otus.service.web.server.ClientWebServer;
import ru.otus.service.web.server.ClientWebServerWithBasicSecurity;
import ru.otus.service.db.TemplateProcessor;
import ru.otus.service.db.TemplateProcessorImpl;

import java.io.IOException;

public class ServerConfigurationBasic implements ServerConfiguration {
    private final DBServiceClient dbServiceClient;

    public ServerConfigurationBasic(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public ClientWebServer configure() throws IOException {
        UserDao userDao = new InMemoryUserDao();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl("/templates/");
        LoginService loginService = new InMemoryLoginServiceImpl(userDao);
        return new ClientWebServerWithBasicSecurity(loginService, templateProcessor, new Server(8080), dbServiceClient);
    }
}
