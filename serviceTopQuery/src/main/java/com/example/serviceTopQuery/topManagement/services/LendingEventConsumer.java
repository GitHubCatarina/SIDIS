package com.example.serviceTopQuery.topManagement.services;

import com.example.serviceTopQuery.topManagement.dto.LendingDTO;
import com.example.serviceTopQuery.topManagement.model.Lending;
import com.example.serviceTopQuery.topManagement.repositories.LendingRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LendingEventConsumer {

    private final TopServiceImpl topService;
    private final LendingRepository lendingRepository;
    private final LendingService lendingService;

    public LendingEventConsumer(TopServiceImpl topService, LendingRepository lendingRepository, LendingService lendingService) {
        this.topService = topService;
        this.lendingRepository = lendingRepository;
        this.lendingService = lendingService;
    }

    // Consumidor para a fila principal de sincronização
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
                // Criar um objeto Lending a partir do LendingDTO
                Lending lending = new Lending();
                lending.setLendingCode(lendingDTO.getLendingCode());
                lending.setReaderId(lendingDTO.getReaderId());
                lending.setBookId(lendingDTO.getBookId());
                lending.setBookTitle(lendingDTO.getBookTitle());
                lending.setLendDate(lendingDTO.getLendDate());
                lending.setLimitDate(lendingDTO.getLimitDate());
                lending.setReturnedDate(lendingDTO.getReturnedDate());
                lending.setReturned(lendingDTO.isReturned());
                lending.setFine(lendingDTO.getFine());
                lending.setComment(lendingDTO.getComment());

                // Usar o método de criação de empréstimo
                lendingRepository.save(lending); // Cria o empréstimo no sistema
                System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " criado com sucesso.");
            }
        }
    }
}