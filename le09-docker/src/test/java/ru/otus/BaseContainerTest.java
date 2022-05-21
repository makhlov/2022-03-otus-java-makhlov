package ru.otus;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;


public abstract class BaseContainerTest {
    private static final int PORT = 8080;
    //если будете повторять, пропишите свой образ
    private static final String imageName = "registry.gitlab.com/petrelevich/dockerregistry/rest-hello:v12";
    private static final GenericContainer<?> REST_CONTAINER =
            new GenericContainer<>(DockerImageName.parse(imageName))
                    .withReuse(true) //To enable reuse of containers, you must set 'testcontainers.reuse.enable=true' in a file located at ~/.testcontainers.properties
                    // (1)   /* Если не устанавливать, то после завершения теста контейнер прибьется с помощью второго спец. контейнера ryuk, он обеспечивает выпил контейнеров по завершению */
                    .withExposedPorts(PORT); //Пробрасываем порт наружу, чтобы можно было на него отправить запрос

    static {
        REST_CONTAINER.start();
    }

    /* (2) Тест контейнер был запущен на основе нашего img на произвольном порту, чтобы не было конфликтов
     * мы не знаем какой порт у нас будет торчать наружу. Поэтому нам надо узнать на каком порту запустился тестовый
     * контейнер. Зная порт мы уже сможем отправить запрос (см. ApplicationTest) */
    public static int getPort() {
        return REST_CONTAINER.getMappedPort(PORT);
    }

    // Мы можем обращаться к контейнеру и выполнять команды. Например, можно получить имя хоста:
    public static String getHost() throws IOException, InterruptedException {
        return REST_CONTAINER.execInContainer("hostname").getStdout().replace("\n","");
    }
}
