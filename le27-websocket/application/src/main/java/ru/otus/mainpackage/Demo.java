package ru.otus.mainpackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.mainpackage.configs.AppConfigForConfigProps;

//http://localhost:8080/actuator/health

@SpringBootApplication
/* Указываем нужный нам класс куда нужно положить property. Когда приложение запустится, Spring обнаружит, что надо
 * создать Bean в который положить Property, находящуюся в application.yaml и имеющую префикс application.
*  */
@EnableConfigurationProperties(AppConfigForConfigProps.class)
public class Demo {

    public static void main(String[] args) {
        SpringApplication.run(Demo.class, args);
    }
}
