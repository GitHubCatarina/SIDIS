package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.lendingManagementCom.dto.LendingDTO;
import com.example.serviceLendingCom.lendingManagementCom.dto.RecomResponseDTO;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import com.example.serviceLendingCom.lendingManagementCom.model.LendingTemp;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

@Service
public class LendingEventConsumer {

    private final LendingServiceImpl lendingService;
    private final RabbitTemplate rabbitTemplate;
    private final LendingTempService lendingTempService;

    public LendingEventConsumer(LendingServiceImpl lendingService, RabbitTemplate rabbitTemplate, LendingTempService lendingTempService) {
        this.lendingService = lendingService;
        this.rabbitTemplate = rabbitTemplate;
        this.lendingTempService = lendingTempService;
    }

    @RabbitListener(queues = "#{lendingQueue.name}", ackMode = "AUTO")
    public void handleLendingEvent(LendingDTO lendingDTO) {
        System.out.println("Mensagem recebida para empréstimo com código: " + lendingDTO.getLendingCode());

        if (lendingDTO.isReturned()) {
            // Passar o comentário, que pode ser nulo ou vazio
            lendingService.markAsReturned(lendingDTO.getLendingCode(), lendingDTO.getComment());
            System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " marcado como devolvido.");
        } else {
            // Verificar se o empréstimo já existe
            boolean lendingExists = lendingService.lendingExists(lendingDTO.getLendingCode());

            if (lendingExists) {
                System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " já existe.");
            } else {
                CreateLendingRequest lendingRequest = new CreateLendingRequest();
                lendingRequest.setReaderId(lendingDTO.getReaderId());
                lendingRequest.setBookId(lendingDTO.getBookId());

                lendingService.createLending(lendingRequest);
                System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " criado.");
            }
        }
    }

    // Consumir eventos da fila "recom"
    @RabbitListener(queues = "recom-to-lending.queue", ackMode = "MANUAL")
    public void handleRecomEvent(LendingTemp lendingTemp, Channel channel, Message message) {
        try {
        System.out.println("Mensagem recebida do recom-to-lending.exchange com Lending Code: " + lendingTemp.getLendingCode());

        // Recuperar o LendingTemp guardado no banco com o mesmo lendingCode
        LendingTemp savedLendingTemp = lendingTempService.findByLendingCode(lendingTemp.getLendingCode());

        if (savedLendingTemp == null) {
                throw new IllegalArgumentException("LendingTemp não encontrado para LendingCode: " + lendingTemp.getLendingCode());
        }

        if (savedLendingTemp != null) {
            // Comparar os parâmetros
            boolean isEqual = true;

            // Comparando todos os campos (exemplo de comparação)
            if (!lendingTemp.getLendingCode().equals(savedLendingTemp.getLendingCode())) {
                isEqual = false;
                System.out.println("LendingCode não é igual.");
            }
            if (lendingTemp.isRecom() != savedLendingTemp.isRecom()) {
                isEqual = false;
                System.out.println("Recomendação não é igual.");
            }
            if (lendingTemp.getCom() == null ? savedLendingTemp.getCom() != null : !lendingTemp.getCom().equals(savedLendingTemp.getCom())) {
                isEqual = false;
                System.out.println("Comentário não é igual.");
            }

            // Se todos os campos forem iguais
            if (isEqual) {
                System.out.println("Os parâmetros de LendingTemp são iguais.");

                // Criar um EditLendingRequest com base no LendingTemp para passar ao método returnBook
                EditLendingRequest editLendingRequest = new EditLendingRequest();
                editLendingRequest.setLendingCode(savedLendingTemp.getLendingCode());
                editLendingRequest.setComment(savedLendingTemp.getCom());  // Ajuste conforme o campo correto
                // Adicione outros campos necessários aqui...

                // Chamar o método returnBook
                lendingService.returnBook(editLendingRequest);


            } else {
                System.out.println("Os parâmetros de LendingTemp não são iguais.");
            }
        } else {
            System.out.println("LendingTemp com o LendingCode " + lendingTemp.getLendingCode() + " não encontrado.");
        }
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
            try {
                // Rejeitar a mensagem e reenviá-la para a fila
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception nackException) {
                System.err.println("Erro ao reenviar mensagem: " + nackException.getMessage());
            }
        }
    }



}