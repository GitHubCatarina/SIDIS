package com.example.serviceRecomCom.recomManagementCom.services;

import com.example.serviceRecomCom.recomManagementCom.dto.RecomDTO;
import com.example.serviceRecomCom.recomManagementCom.model.LendingTemp;
import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RecomEventConsumer {

    private final RecomServiceImpl recomService;
    private final RecomEventProducer recomEventProducer;

    public RecomEventConsumer(RecomServiceImpl recomService, RecomEventProducer recomEventProducer) {
        this.recomService = recomService;
        this.recomEventProducer = recomEventProducer;
    }

    // Consumidor para a fila principal de sincronização
    @RabbitListener(queues = "#{recomQueue.name}", ackMode = "AUTO")
    public void handleRecomCreatedEvent(RecomDTO recomDTO) {
        System.out.println("Mensagem recebida para recomendação com ID: " + recomDTO.getId());

        // Verificar se o Recom já existe pelo “ID”
        boolean recomExists = recomService.existsById(recomDTO.getId());

        if (recomExists) {
            System.out.println("Recomendação com ID " + recomDTO.getId() + " já existe, não será criada.");
        } else {
            // Criar um objeto Recom a partir do RecomDTO
            Recom recom = new Recom();
            recom.setId(recomDTO.getId());
            recom.setLendingId(recomDTO.getLendingId());  // Aqui usamos LendingId que é do tipo Long
            recom.setRecommend(recomDTO.getRecommend());
            recom.setComment(recomDTO.getComment());

            // Usar o método de criação de Recom
            recomService.createRecom(recom);  // Cria a recomendação no sistema
            System.out.println("Recomendação com ID " + recomDTO.getId() + " criada com sucesso.");
        }
    }


    @RabbitListener(queues = "recomlending.queue", ackMode = "MANUAL")
    public void handleLendingTempEvent(LendingTemp lendingTemp, Message message, Channel channel) throws IOException {
            // Verificar se o LendingId já existe
            if (recomService.existsByLendingId(lendingTemp.getLendingCode())) {
                System.out.println("LendingId já existe. Rejeitando a mensagem: " + lendingTemp.getLendingCode());
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false); // Rejeitar sem reencaminhar para a fila
                return;
            }

            // Processar a mensagem recebida
            System.out.println("Mensagem recebida para LendingTemp:");
            System.out.println("Lending Code: " + lendingTemp.getLendingCode());

            // Criar o objeto Recom baseado nas informações de LendingTemp
            Recom recom = new Recom();
            recom.setLendingId(lendingTemp.getLendingCode());  // Usando o LendingTemp ID
            recom.setRecommend(lendingTemp.isRecom());  // A recomendação recebida (true ou false)
            recom.setComment(lendingTemp.getCom());  // O comentário da devolução

            // Salvar o recom no banco de dados
            Recom savedRecom = recomService.createRecom(recom);

            // Converter para DTO e enviar evento para RabbitMQ
            recomEventProducer.sendRecomCreatedEvent(savedRecom.toDTO());


            // Enviar o LendingTemp de volta ao Lending
            recomEventProducer.sendLendingTempBack(lendingTemp);

            // Imprimir confirmação de envio do evento
            System.out.println("Recom criado e evento enviado com ID: " + savedRecom.getId());

            // Confirmar a mensagem
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }
}