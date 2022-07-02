package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.necessary.repository.executor.DbExecutorImpl;
import ru.otus.necessary.sessionmanager.TransactionRunnerJdbc;
import ru.otus.necessary.crm.datasource.DriverManagerDataSource;
import ru.otus.necessary.crm.model.Client;
import ru.otus.necessary.crm.model.Manager;
import ru.otus.necessary.crm.service.DbServiceClientImpl;
import ru.otus.necessary.crm.service.DbServiceManagerImpl;

import ru.otus.homework.DataTemplateJdbc;
import ru.otus.homework.entity.clazz.EntityClassMetaData;
import ru.otus.homework.entity.clazz.EntityClassMetaDataImpl;
import ru.otus.homework.entity.sql.EntitySQLMetaData;
import ru.otus.homework.entity.sql.EntitySQLMetaDataImpl;

import javax.sql.DataSource;

public class HomeWorkDemo {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWorkDemo.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<Client>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

// Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

// Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
