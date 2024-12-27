package com.example.serviceRecomCom.recomManagementCom.repositories;

import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomRepository extends JpaRepository<Recom, Long> {
    // Método para buscar Recom por lendingId
    Recom findByLendingId(String lendingId);
}
