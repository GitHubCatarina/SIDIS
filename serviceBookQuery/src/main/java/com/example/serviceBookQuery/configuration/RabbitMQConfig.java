package com.example.serviceBookQuery.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    // Exchange para comunicação entre instâncias
    @Bean
    public FanoutExchange bookExchange() {
        return new FanoutExchange("book.exchange");
    }


    // Filas para as instâncias
    @Bean
    public Queue bookQueue() {
        return new Queue("book.queue." + UUID.randomUUID(), true, true, true); // Nome único
    }


    // Binding para a fila principal
    @Bean
    public Binding bookBinding(Queue bookQueue, FanoutExchange bookExchange) {
        return BindingBuilder.bind(bookQueue).to(bookExchange);
    }

    // Converter de mensagem
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // Usando Jackson para converter objetos em JSON
    }
}
