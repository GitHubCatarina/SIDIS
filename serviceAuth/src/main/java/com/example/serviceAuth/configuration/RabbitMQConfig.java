package com.example.serviceAuth.configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange para comunicação entre instâncias
    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange("auth.exchange");
    }

    // Queue para receber mensagens de sincronização
    @Bean
    public Queue authQueue() {
        return new Queue("auth.queue");
    }

    // Bind queue ao exchange
    @Bean
    public Binding authBinding(Queue authQueue, TopicExchange authExchange) {
        return BindingBuilder.bind(authQueue).to(authExchange).with("auth.user.created");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // Usando Jackson para converter objetos em JSON
    }
}
