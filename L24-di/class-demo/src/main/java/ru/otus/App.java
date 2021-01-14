package ru.otus;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.config.AppConfig;
import ru.otus.services.GameProcessor;


public class App {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-context.xml");
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        GameProcessor gameProcessor = ctx.getBean(GameProcessor.class);
        gameProcessor.startGame();
    }
}
