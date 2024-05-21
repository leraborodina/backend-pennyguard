package ru.itcolleg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS).
 * This class configures CORS settings for the entire application, allowing requests from specified origins,
 * methods, headers, and credentials. It also provides a RestTemplate bean for making HTTP requests.
 * <p>
 * Класс конфигурации для обработки междоменного обмена ресурсами (CORS).
 * Этот класс настраивает параметры CORS для всего приложения, разрешая запросы из указанных источников,
 * методов, заголовков и учетных данных. Он также предоставляет бин RestTemplate для выполнения HTTP-запросов.
 */

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configure CORS settings for the entire application.
     * Настройка параметров CORS для всего приложения.
     *
     * @param registry The CorsRegistry instance to configure CORS mappings.
     *                 Экземпляр CorsRegistry для настройки отображений CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Map all endpoints.
                .allowedOrigins("http://localhost:4200") // Allow requests from localhost:4200.
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specified HTTP methods.
                .allowedHeaders("*") // Allow all headers.
                .allowCredentials(true) // Allow credentials (e.g., cookies).
                .maxAge(3600); // Set max age of preflight requests to 3600 seconds (1 hour).
    }

    /**
     * Provides a RestTemplate bean.
     * Предоставляет бин RestTemplate.
     *
     * @return RestTemplate bean instance.
     * Экземпляр бина RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
