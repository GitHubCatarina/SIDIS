package com.example.serviceRecomCom.recomManagementCom.services;

import com.example.serviceRecomCom.recomManagementCom.model.Recom;

import java.util.Optional;

public interface RecomService {
    Recom createRecom(Recom recom);
    Optional<Recom> getRecomById(Long id);
    Recom getRecomByLendingId(String lendingId);
    Recom updateRecom(Long id, Recom recomDetails);
    void deleteRecom(Long id);
}
