package com.example.serviceReaderCom.readerManagement.services;


import com.example.serviceReaderCom.readerManagement.dto.ReaderDTO;
import com.example.serviceReaderCom.readerManagement.model.Reader;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class ReaderEventConsumer {

    private final ReaderServiceImpl readerService;

    public ReaderEventConsumer(ReaderServiceImpl readerService) {
        this.readerService = readerService;
    }

    // Consumidor para a fila principal de sincronização
    @RabbitListener(queues = "#{readerQueue.name}", ackMode = "AUTO")
    public void handleReaderCreatedEvent(ReaderDTO readerDTO) {
        System.out.println("Mensagem recebida para leitor com id: " + readerDTO.getReaderCode());

        // Verificar se o leitor já existe pelo readerCode
        boolean readerExists = readerService.readerExists(readerDTO.getReaderCode());

        if (readerExists) {
            System.out.println("Leitor com código " + readerDTO.getReaderCode() + " já existe, não será criado.");
        } else {
            // Criar um EditReaderRequest a partir do ReaderDTO
            EditReaderRequest editReaderRequest = new EditReaderRequest(
                    readerDTO.getName(),
                    readerDTO.getEmail(),
                    readerDTO.getDateOfBirth(),
                    readerDTO.getPhoneNumber(),
                    readerDTO.getGDBRConsent(),
                    readerDTO.getInterests()
            );

            // Como não há um arquivo MultipartFile, pode ser usado null
            readerService.createReader(editReaderRequest, null);
            System.out.println("Leitor com código " + readerDTO.getReaderCode() + " criado com sucesso.");
        }
    }
}
