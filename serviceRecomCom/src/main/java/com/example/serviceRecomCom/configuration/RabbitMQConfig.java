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
    public DirectExchange recomlendingExchange() {
        return new DirectExchange("recomlending.exchange");
    }

    // Filas para as instâncias
    @Bean
    public Queue recomQueue() { return new Queue("recom.queue." + UUID.randomUUID(), true, true, true); } // Nome único

    @Bean
    public Queue recomlendingQueue() {
        return new Queue("recomlending.queue", true, false, false); // Fila compartilhada, será usada por todas as instâncias
    }

    // Binding para a fila principal
    @Bean
    public Binding recomBinding(Queue recomQueue, FanoutExchange recomExchange) {
        return BindingBuilder.bind(recomQueue).to(recomExchange);
    }

    @Bean
    public Binding recomlendingBinding(Queue recomlendingQueue, DirectExchange recomlendingExchange) {
        return BindingBuilder.bind(recomlendingQueue).to(recomlendingExchange).with("recomlending.routingKey"); // A mesma chave de roteamento usada em lending
    }



    @Bean
    public FanoutExchange recomToLendingExchange() {
        return new FanoutExchange("recom-to-lending.exchange");
    }

    @Bean
    public Queue recomToLendingQueue() {
        return new Queue("recom-to-lending.queue", true, false, false); // Fila persistente, sem UUID
    }


    @Bean
    public Binding recomToLendingBinding(Queue recomToLendingQueue, FanoutExchange recomToLendingExchange) {
        return BindingBuilder.bind(recomToLendingQueue).to(recomToLendingExchange);
    }


    // Converter de mensagem
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // Usando Jackson para converter objetos em JSON
    }
}
