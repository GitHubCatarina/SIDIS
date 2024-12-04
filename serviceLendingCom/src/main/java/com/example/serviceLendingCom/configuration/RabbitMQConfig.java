package com.example.serviceLendingCom.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    // Exchange para comunicação entre lending
    @Bean
    public FanoutExchange lendingExchange() { return new FanoutExchange("lending.exchange");}

    @Bean
    public FanoutExchange bookExchange() {
        return new FanoutExchange("book.exchange");
    }

    @Bean
    public FanoutExchange readerExchange() {
        return new FanoutExchange("reader.exchange");
    }


    // Filas para as instâncias
    @Bean
    public Queue lendingQueue() { return new Queue("lending.queue." + UUID.randomUUID(), true, true, true); } // Nome único

    @Bean
    public Queue bookQueue() {
        return new Queue("book.queue." + UUID.randomUUID(), true, true, true); // Nome único
    }
    @Bean
    public Queue readerQueue() { return new Queue("reader.queue." + UUID.randomUUID(), true, true, true);} // Nome único


    // Binding para a fila principal
    @Bean
    public Binding lendingBinding(Queue lendingQueue, FanoutExchange lendingExchange) {
        return BindingBuilder.bind(lendingQueue).to(lendingExchange);
    }

    @Bean
    public Binding bookBinding(Queue bookQueue, FanoutExchange bookExchange) {
        return BindingBuilder.bind(bookQueue).to(bookExchange);
    }

    @Bean
    public Binding readerBinding(Queue readerQueue, FanoutExchange readerExchange) {
        return BindingBuilder.bind(readerQueue).to(readerExchange);
    }


    // Converter de mensagem
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // Usando Jackson para converter objetos em JSON
    }
}
