package com.example.serviceReaderQuery.configuration;

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
    public FanoutExchange readerExchange() {
        return new FanoutExchange("reader.exchange");
    }


    // Filas para as instâncias
    @Bean
    public Queue readerQueue() {
        return new Queue("reader.queue." + UUID.randomUUID(), true, true, true); // Nome único
    }


    // Binding para a fila principal
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
