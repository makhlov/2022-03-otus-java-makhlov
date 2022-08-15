package ru.otus.mainpackage.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

/* Нас интересует конкретный раздел файла с пропертями, поэтому мы указываем
 * нужный нам префикс. */
@ConfigurationProperties(prefix = "application")
public class AppConfigForConfigProps {

    /* Property про rest нас не интересуют, поэтому мы их пропускаем и прописываем только paramName */
    private String paramName;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}

/* После внесения всех нужных полей и установки префикса нужно как-то сказать Spring, что в этот класс надо положить
 * значения property. Для этого мы идем в класс конфигурации (тут Demo.class) и указываем через
 * @EnableConfigurationProperties нужный нам класс
 */