package com.example.serviceRecomCom.recomManagementCom.services;

import com.example.serviceRecomCom.recomManagementCom.dto.RecomDTO;
import com.example.serviceRecomCom.recomManagementCom.model.LendingTemp;
import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

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

    @RabbitListener(queues = "#{recomlendingQueue.name}", ackMode = "AUTO")
    public void handleLendingTempEvent(LendingTemp lendingTemp) {
        // Imprimir o conteúdo do LendingTemp recebido
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

        // Imprimir confirmação de envio do evento
        System.out.println("Recom criado e evento enviado com ID: " + savedRecom.getId());
    }
}