package com.example.serviceRecomCom.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.AllowedListDeserializingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitMQConfig {

    // Exchange para comunicação entre recom
    @Bean
    public FanoutExchange recomExchange() { return new FanoutExchange("recom.exchange");}

    @Bean
    public FanoutExchange recomlendingExchange() {
        return new FanoutExchange("recomlending.exchange");
    }


    // Filas para as instâncias
    @Bean
    public Queue recomQueue() { return new Queue("recom.queue." + UUID.randomUUID(), true, true, true); } // Nome único

    @Bean
    public Queue recomlendingQueue() {
        return new Queue("recomlending.queue." + UUID.randomUUID(), true, true, true); // Nome único
    }

    // Binding para a fila principal
    @Bean
    public Binding recomBinding(Queue recomQueue, FanoutExchange recomExchange) {
        return BindingBuilder.bind(recomQueue).to(recomExchange);
    }

    @Bean
    public Binding recomlendingBinding(Queue recomlendingQueue, FanoutExchange recomlendingExchange) {
        return BindingBuilder.bind(recomlendingQueue).to(recomlendingExchange);
    }



    // Converter de mensagem
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // Usando Jackson para converter objetos em JSON
    }
}
