package ru.itcolleg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main application class.
 * Класс главного приложения.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class Application {


    /**
     * The main method to start the Spring Boot application.
     * Основной метод для запуска приложения Spring Boot.
     *
     * @param args The command line arguments.
     *             Аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
