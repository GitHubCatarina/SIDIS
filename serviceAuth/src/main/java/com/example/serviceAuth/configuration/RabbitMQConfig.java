package com.example.serviceAuth.configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    // Exchange para comunicação entre instâncias
    @Bean
    public FanoutExchange authExchange() {
        return new FanoutExchange("auth.exchange");
    }


    // Filas para as instâncias
    @Bean
    public Queue authQueue() {
        return new Queue("auth.queue." + UUID.randomUUID(), true, true, true); // Nome único
    }


    // Binding para a fila principal
    @Bean
    public Binding authBinding(Queue authQueue, FanoutExchange authExchange) {
        return BindingBuilder.bind(authQueue).to(authExchange);
    }

    // Converter de mensagem
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // Usando Jackson para converter objetos em JSON
    }
}
