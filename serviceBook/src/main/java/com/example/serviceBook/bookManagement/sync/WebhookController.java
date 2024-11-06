package com.example.serviceBook.bookManagement.sync;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final SyncService syncService;

    public WebhookController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PutMapping("/sync")
    public ResponseEntity<String> handleWebhook(@RequestBody SyncRequest syncRequest) {
        System.out.println("Sync Request: " + syncRequest);
        syncService.syncData(syncRequest);
        return ResponseEntity.ok("Sincronização recebida com sucesso");
    }
}
