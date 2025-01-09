package com.example.serviceRecomCom.recomManagementCom.services;

import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import com.example.serviceRecomCom.recomManagementCom.repositories.RecomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecomServiceImpl implements RecomService {

    private final RecomRepository recomRepository;

    public RecomServiceImpl(RecomRepository recomRepository) {
        this.recomRepository = recomRepository;
    }

    @Override
    public Recom createRecom(Recom recom) {
        return recomRepository.save(recom);
    }

    @Override
    public Optional<Recom> getRecomById(Long id) {
        return recomRepository.findById(id);
    }

    @Override
    public Recom getRecomByLendingId(String lendingId) {
        return recomRepository.findByLendingId(lendingId);
    }

    @Override
    public Recom updateRecom(Long id, Recom recomDetails) {
        Optional<Recom> existingRecom = recomRepository.findById(id);
        if (existingRecom.isPresent()) {
            Recom recom = existingRecom.get();
            recom.setLendingId(recomDetails.getLendingId());
            recom.setRecommend(recomDetails.getRecommend());
            recom.setComment(recomDetails.getComment());
            return recomRepository.save(recom);
        }
        return null;  // ou pode lançar uma exceção, dependendo da lógica desejada
    }

    @Override
    public void deleteRecom(Long id) {
        Optional<Recom> recom = recomRepository.findById(id);
        recom.ifPresent(recomRepository::delete);
    }

    public boolean existsById(Long id) {
        return recomRepository.existsById(id);  // Usando Long como tipo do ID
    }
}

