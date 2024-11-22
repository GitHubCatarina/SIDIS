package com.example.serviceBookCom.authorManagementCom.syncAuthor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author/webhook")
public class AuthorWebhookController {

    private final AuthorSyncService syncService;

    public AuthorWebhookController(AuthorSyncService syncService) {
        this.syncService = syncService;
    }

    @PutMapping("/sync")
    public ResponseEntity<String> handleWebhook(@RequestBody AuthorSyncRequest syncRequest) {
        System.out.println("Sync Request: " + syncRequest);
        syncService.syncData(syncRequest);
        return ResponseEntity.ok("Sincronização recebida com sucesso");
    }
}
