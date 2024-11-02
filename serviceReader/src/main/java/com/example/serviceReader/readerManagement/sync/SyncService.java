package com.example.serviceReader.readerManagement.sync;

import org.springframework.stereotype.Service;

@Service
public class SyncService {

    public void syncData(SyncRequest syncRequest) {
        if ("create".equals(syncRequest.getAction())) {
            // Lógica para adicionar os dados do novo leitor
            System.out.println("Sincronizando novo leitor com ID: " + syncRequest.getReaderId());
        } else if ("update".equals(syncRequest.getAction())) {
            // Lógica para atualizar os dados do leitor
            System.out.println("Sincronizando atualização do leitor com ID: " + syncRequest.getReaderId());
        } else if ("delete".equals(syncRequest.getAction())) {
            // Lógica para remover o leitor
            System.out.println("Sincronizando remoção do leitor com ID: " + syncRequest.getReaderId());
        }
    }
}

