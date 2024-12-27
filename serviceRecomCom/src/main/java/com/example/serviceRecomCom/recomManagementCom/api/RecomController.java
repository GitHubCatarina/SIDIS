package com.example.serviceRecomCom.recomManagementCom.api;

import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import com.example.serviceRecomCom.recomManagementCom.repositories.RecomRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/recoms")
public class RecomController {

    private final RecomRepository recomRepository;

    public RecomController(RecomRepository recomRepository) {
        this.recomRepository = recomRepository;
    }

    // Endpoint para criar um Recom
    @PostMapping
    public ResponseEntity<Recom> createRecom(@Valid @RequestBody Recom recom) {
        Recom savedRecom = recomRepository.save(recom);
        return ResponseEntity.ok(savedRecom);
    }

    // Endpoint para buscar um Recom por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recom> getRecomById(@PathVariable Long id) {
        Optional<Recom> recom = recomRepository.findById(id);
        return recom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para buscar um Recom por LendingId
    @GetMapping("/lending/{lendingId}")
    public ResponseEntity<Recom> getRecomByLendingId(@PathVariable String lendingId) {
        Recom recom = recomRepository.findByLendingId(lendingId);
        if (recom != null) {
            return ResponseEntity.ok(recom);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para atualizar um Recom
    @PutMapping("/{id}")
    public ResponseEntity<Recom> updateRecom(@PathVariable Long id, @Valid @RequestBody Recom recomDetails) {
        Optional<Recom> existingRecom = recomRepository.findById(id);
        if (existingRecom.isPresent()) {
            Recom recom = existingRecom.get();
            recom.setLendingId(recomDetails.getLendingId());
            recom.setRecommend(recomDetails.getRecommend());
            recom.setComment(recomDetails.getComment());
            Recom updatedRecom = recomRepository.save(recom);
            return ResponseEntity.ok(updatedRecom);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para deletar um Recom
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecom(@PathVariable Long id) {
        Optional<Recom> recom = recomRepository.findById(id);
        if (recom.isPresent()) {
            recomRepository.delete(recom.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
