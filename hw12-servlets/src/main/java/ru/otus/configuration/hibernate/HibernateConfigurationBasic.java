package ru.otus.configuration.hibernate;

import org.hibernate.cfg.Configuration;
import ru.otus.persistance.repository.DataTemplateHibernate;
import ru.otus.persistance.repository.HibernateUtils;
import ru.otus.persistance.sessionmanager.TransactionManagerHibernate;
import ru.otus.persistance.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;
import ru.otus.service.db.DBServiceClient;
import ru.otus.service.db.DbServiceClientImpl;

public class HibernateConfigurationBasic implements HibernateConfiguration {

    public static final String HIBERNATE_CONFIGURATION_LOCATION = "hibernate.cfg.xml";

    @Override
    public DBServiceClient configure() {
        var config = new Configuration().configure(HIBERNATE_CONFIGURATION_LOCATION);

        var url = config.getProperty("hibernate.connection.url");
        var userName = config.getProperty("hibernate.connection.username");
        var password = config.getProperty("hibernate.connection.password");

        MigrationsExecutorFlyway migrations = new MigrationsExecutorFlyway(url, userName, password);
        migrations.executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(config, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }
}
