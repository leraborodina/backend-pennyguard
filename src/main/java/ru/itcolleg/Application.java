package ru.itcolleg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
   When you annotate a class with @SpringBootApplication,
   you are essentially declaring that it is the main class of a Spring Boot application.

   In the context of Spring Framework, the term "IoC" stands for Inversion of Control.
   The IoC container is responsible for managing the lifecycle of Java objects (beans),
   resolving their dependencies, and making them available for use throughout your application.

   The @SpringBootApplication annotation combines 3 major annotations:

   1) Configuration: Indicates that the class can be used by the Spring IoC container as a source of bean definitions.
   Example:
   @Bean
   public MyService myService() {
   return new MyService();
   }

   2) EnableAutoConfiguration: Auto-configuration mechanism, which automatically configures your Spring application based on the dependencies you have added to the project.
   Example:
   - Classpath Scanning
   - Embedded Web Server Configuration
   - Properties Configuration
   - Database Auto-Configuration
   - Security Configuration

   3) ComponentScan: Tells Spring to scan and discover other Spring components
   (such as controllers, services, and repositories) in the current package and its subpackages.

   Spring identifies classes annotated with @Component, @Controller, @Service, and @Repository
   (or other custom annotations marked as components).

   It registers these classes as beans in the Spring IoC container.

   If a component has dependencies (other beans it relies on),
   Spring injects those dependencies automatically.
   This process is known as dependency injection.

   Spring manages the lifecycle of these beans.
   It creates instances of the beans, initializes them (calling @PostConstruct methods if present),
   and, if configured, can call destruction methods (@PreDestroy) when the application shuts down.

   Spring performs autowiring, resolving dependencies by type or name.
   This means that if one bean needs another, Spring can automatically inject the required bean.

   Once the scanning and registration are complete, the beans are ready for use throughout your application.
   For example, if you have a controller annotated with @Controller, it's ready to handle web requests.
   If you have a service annotated with @Service, it's ready to provide business logic.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
