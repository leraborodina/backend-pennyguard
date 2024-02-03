package ru.itcolleg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS).
 */
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configure CORS settings for the entire application.
     *
     * @param registry The CorsRegistry instance to configure CORS mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Step 1: Allow CORS for all paths
                .allowedOrigins("http://localhost:4200")  // Step 2: Specify the allowed origin (e.g., Angular app)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Step 3: Specify the allowed HTTP methods
                .allowedHeaders("*")  // Step 4: Allow any headers
                .allowCredentials(true)  // Step 5: Allow credentials (e.g., cookies)
                .maxAge(3600);  // Step 6: Set the maximum age of the preflight request (in seconds)
    }
}
