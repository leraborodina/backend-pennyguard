package ru.itcolleg.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

@Configuration
public class WebSocketConfig {

    @Bean
    public MessageChannel messageChannel() {
        return new ExecutorSubscribableChannel();
    }

    @Bean
    public SimpMessagingTemplate messagingTemplate() {
        return new SimpMessagingTemplate(messageChannel());
    }
}

