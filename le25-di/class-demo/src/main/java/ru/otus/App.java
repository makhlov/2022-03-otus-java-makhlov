package ru.otus;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.services.*;


public class App {
    public static void main(String[] args) throws Exception {
        /*
        //Ручная настройка:
        EquationPreparer equationPreparer = new EquationPreparerImpl();
        IOService ioService = new IOServiceStreams(System.out, System.in);
        PlayerService playerService = new PlayerServiceImpl(ioService);
        GameProcessor gameProcessor = new GameProcessorImpl(ioService, equationPreparer, playerService);
         */

        //Первый вариант: на основе XML
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-context.xml");

        //Второй вариант: на основе аннотаций
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        GameProcessor gameProcessor = ctx.getBean(GameProcessor.class);

        gameProcessor.startGame();
    }
}
