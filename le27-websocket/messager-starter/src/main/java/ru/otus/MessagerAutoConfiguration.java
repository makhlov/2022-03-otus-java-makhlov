package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
/* Если в зависимостях (в classpath) есть класс Messager, значит пользователь хочет с ним поработать
 * Тогда надо создать этот конфигурационный бин (иначе зачем он?) */
@ConditionalOnClass(Messager.class)
@EnableConfigurationProperties(Props.class)
public class MessagerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MessagerAutoConfiguration.class);

    private final Props props;

    public MessagerAutoConfiguration(Props props) {
        this.props = props;
    }

    /* Что будет создаваться: */
    @Bean
    /* Во первых, будет создаваться MessagerConfig. Создастся он только в том случае, если бина
     * messagerConfig еще нет. Наличие бина проверяется, т.к. пользователь может пользоваться нашим
     * стартером, но при этом бин конфигурации сделает самостоятельно. Например, парамер для message он
     * может брать из БД, а мы рассчитываем из yaml. Чтобы не было конфликтов новый мы создавать не будем */
    @ConditionalOnMissingBean
    public MessagerConfig messagerConfig() {
        /* Если пользователь не определил значение в yaml, то мы укажем дефолтные */
        var message = props.getMessage() == null ? "default message" : props.getMessage();
        logger.info("AutoConfig. creating MessagerConfig, default message:{}", message);
        return new MessagerConfig(message);
    }

    @Bean
    /* Аналогично конфигу, messager можно сделать самостоятельно, поэтому проверяем нет ли его бина */
    @ConditionalOnMissingBean
    public Messager messager(MessagerConfig messagerConfig) {
        /* Если его нет - то мы его создадим */
        logger.info("AutoConfig. creating Messager");
        return new Messager(messagerConfig);
    }
}
