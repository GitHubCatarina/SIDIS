package com.example.serviceRecomCom.recomManagementCom.api;

import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import com.example.serviceRecomCom.recomManagementCom.services.RecomService;
import com.example.serviceRecomCom.recomManagementCom.services.RecomEventProducer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/recoms")
public class RecomController {

    private final RecomService recomService;
    private final RecomEventProducer recomEventProducer;

    public RecomController(RecomService recomService, RecomEventProducer recomEventProducer) {
        this.recomService = recomService;
        this.recomEventProducer = recomEventProducer;
    }

    // Endpoint para criar um Recom
    @PostMapping
    public ResponseEntity<Recom> createRecom(@Valid @RequestBody Recom recom) {
        Recom savedRecom = recomService.createRecom(recom);
        //TODO eliminar todos o os "supondo"
        // Enviar um evento para o EventProducer para atualizar outras instâncias
        recomEventProducer.sendRecomCreatedEvent(savedRecom.toDTO());  // Supondo que você tem um metodo toDTO() para converter para RecomDTO

        return ResponseEntity.ok(savedRecom);
    }

    // Endpoint para buscar um Recom por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recom> getRecomById(@PathVariable Long id) {
        Optional<Recom> recom = recomService.getRecomById(id);
        return recom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para buscar um Recom por LendingId
    @GetMapping("/lending/{lendingId}")
    public ResponseEntity<Recom> getRecomByLendingId(@PathVariable String lendingId) {
        Recom recom = recomService.getRecomByLendingId(lendingId);
        if (recom != null) {
            return ResponseEntity.ok(recom);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para atualizar um Recom
    @PutMapping("/{id}")
    public ResponseEntity<Recom> updateRecom(@PathVariable Long id, @Valid @RequestBody Recom recomDetails) {
        Recom updatedRecom = recomService.updateRecom(id, recomDetails);
        if (updatedRecom != null) {
            return ResponseEntity.ok(updatedRecom);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para eliminar um Recom
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecom(@PathVariable Long id) {
        recomService.deleteRecom(id);
        return ResponseEntity.noContent().build();
    }
}