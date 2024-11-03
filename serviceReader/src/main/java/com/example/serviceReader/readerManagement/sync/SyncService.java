package com.example.serviceReader.readerManagement.sync;

import com.example.serviceReader.readerManagement.api.ReaderProfileView;
import com.example.serviceReader.readerManagement.api.ReaderProfileViewMapper;
import com.example.serviceReader.readerManagement.api.ReaderProfileViewMapperImpl;
import com.example.serviceReader.readerManagement.model.Reader;
import com.example.serviceReader.readerManagement.repositories.ReaderRepository;
import com.example.serviceReader.readerManagement.services.ReaderService;
import com.example.serviceReader.readerManagement.services.EditReaderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SyncService {
    @Autowired
    private ReaderProfileViewMapper readerProfileViewMapper;
    private final RestTemplate restTemplate;
    @Value("${webhook.url}")
    private String webhookUrl; // URL da outra instância
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private ReaderService readerService;
    private Reader reader;


    public SyncService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void syncData(SyncRequest syncRequest) {
        EditReaderRequest editRequest = syncRequest.getResource();

        if (editRequest != null) {
            if (syncRequest.getResource().getName() != null) editRequest.setName(syncRequest.getResource().getName());
            if (syncRequest.getResource().getEmail() != null) editRequest.setEmail(syncRequest.getResource().getEmail());
            if (syncRequest.getResource().getDateOfBirth() != null) editRequest.setDateOfBirth(syncRequest.getResource().getDateOfBirth());
            if (syncRequest.getResource().getPhoneNumber() != null) editRequest.setPhoneNumber(syncRequest.getResource().getPhoneNumber());
            if (syncRequest.getResource().getGDBRConsent() != null) editRequest.setGDBRConsent(syncRequest.getResource().getGDBRConsent());
            if (syncRequest.getResource().getInterests() != null) editRequest.setInterests(syncRequest.getResource().getInterests());
        }


        if ("create".equals(syncRequest.getAction())) {
            readerService.createReader(editRequest, null);
            System.out.println("Sincronizando novo leitor com ID: " + syncRequest.getReaderId());
        } else if ("update".equals(syncRequest.getAction())) {
            readerService.updateReader(syncRequest.getReaderId(), editRequest, syncRequest.getDesiredVersion());
            System.out.println("Sincronizando atualização do leitor com ID: " + syncRequest.getReaderId());
        } else if ("delete".equals(syncRequest.getAction())) {
            readerRepository.deleteById(syncRequest.getReaderId());
            System.out.println("Sincronizando remoção do leitor com ID: " + syncRequest.getReaderId());
        }
        sendWebhookToOtherInstance(syncRequest);
    }

    public void sendWebhookToOtherInstance(SyncRequest syncRequest) {
        try {
            restTemplate.postForEntity(webhookUrl + "/webhook/sync", syncRequest, String.class);
            System.out.println("Webhook enviado para a outra instância");
        } catch (Exception e) {
            System.err.println("Erro ao enviar webhook para a outra instância: " + e.getMessage());
        }
    }
}

