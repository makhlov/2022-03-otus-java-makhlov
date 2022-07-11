import org.junit.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.repository.ClientDataTemplateJdbc;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbServiceClientImplTest {
    DbServiceClientImpl dbServiceClient;

    private final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping(
                    "V1__initial_schema.sql",
                    "/docker-entrypoint-initdb.d/V1__initial_schema.sql", BindMode.READ_ONLY
            );

    private void prepareDbServiceClientImpl() {
        var dataSource = new DriverManagerDataSource(
                postgresqlContainer.getJdbcUrl(),
                postgresqlContainer.getUsername(),
                postgresqlContainer.getPassword()
        );
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var clientTemplate = new ClientDataTemplateJdbc(new DbExecutorImpl());

        dbServiceClient = new DbServiceClientImpl(transactionRunner, clientTemplate);
    }

    @Test
    public void checkingAccelerationWhenUsingCache() {
        postgresqlContainer.start();
        prepareDbServiceClientImpl();

        //given
        List<Long> ids = new ArrayList<>();
        int amount = 151;
        for (int i = 1; i < amount; i++) {
            var testClient = new Client("client" + i);
            ids.add(dbServiceClient.saveClient(testClient).getId());
        }

        //when
        long withoutCache = performSelectionByIds(ids);
        long withCache = performSelectionByIds(ids);

        //then
        assertTrue(withCache < withoutCache);
        postgresqlContainer.stop();
    }

    private long performSelectionByIds(final List<Long> ids) {
        var start = System.currentTimeMillis();
        for (var id : ids) {
            dbServiceClient.getClient(id);
        }
        var end = System.currentTimeMillis();
        return end - start;
    }
}
