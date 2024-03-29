package ru.otus.mainpackage.configs;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    //http://localhost:8080/swagger-ui/index.html

    @Bean
    /*Создаем bean для swagger, в @Value прописываем префикс для указания точки откуда строить
     *доку. Рекомендовано использовать версионирование api. В дальнейшем при переходе с v1 на м2
     *просто указываем новую версию в @Value, как указано ниже */
    public GroupedOpenApi publicApi(@Value("${application.rest.api.prefix}/v2")String prefix) {
        return GroupedOpenApi.builder()
                .group("DemoApplication")
                .pathsToMatch(String.format("%s/**", prefix))
                .build();
    }
}
