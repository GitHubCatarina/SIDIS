package com.example.serviceLendingCom.lendingManagementCom.repositories;

import com.example.serviceLendingCom.lendingManagementCom.model.LendingTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingTempRepository extends JpaRepository<LendingTemp, Long> {
}
