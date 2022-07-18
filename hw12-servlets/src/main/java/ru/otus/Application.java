package ru.otus;

import ru.otus.configuration.hibernate.HibernateConfiguration;
import ru.otus.configuration.hibernate.HibernateConfigurationBasic;
import ru.otus.configuration.server.ServerConfigurationBasic;

public class Application {
    public static void main(String[] args) throws Exception {
        HibernateConfiguration hibernateConfiguration = new HibernateConfigurationBasic();

        var dbServiceClient = hibernateConfiguration.configure();
        var webServerConfiguration = new ServerConfigurationBasic(dbServiceClient);
        var webServer = webServerConfiguration.configure();

        webServer.start();
        webServer.join();
    }
}
