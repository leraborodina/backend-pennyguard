package ru.itcolleg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * Configuration class for WebSocket setup.
 * This class sets up WebSocket communication between the server and clients.
 * It enables messaging for topics and sets up the application's messaging prefixes.
 * Additionally, it registers WebSocket endpoints and allows connections from specific origins using SockJS.
 * <p>
 * Класс конфигурации для настройки WebSocket.
 * Этот класс настраивает обмен данными по WebSocket между сервером и клиентами.
 * Он активирует обмен сообщениями для различных тем и устанавливает префиксы для сообщений в приложении.
 * Кроме того, он регистрирует конечные точки WebSocket и позволяет подключения с определенных источников с помощью SockJS.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures message broker options.
     * Включает простой брокер для сообщений на основе тем и устанавливает префикс назначения приложения.
     *
     * @param config Экземпляр MessageBrokerRegistry для настройки.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/notifications");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers WebSocket endpoints.
     * Регистрирует конечные точки WebSocket и разрешает подключения из указанных источников с использованием SockJS.
     *
     * @param registry Экземпляр StompEndpointRegistry для регистрации конечных точек.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }

}

