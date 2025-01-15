package com.example.serviceLendingCom.lendingManagementCom.services;


import com.example.serviceLendingCom.exceptions.NotFoundException;
import com.example.serviceLendingCom.lendingManagementCom.dto.ReturnLendingTempRequest;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import com.example.serviceLendingCom.lendingManagementCom.model.LendingTemp;
import com.example.serviceLendingCom.lendingManagementCom.repositories.LendingRepository;
import com.example.serviceLendingCom.lendingManagementCom.repositories.LendingTempRepository;
import org.springframework.stereotype.Service;


@Service
public class LendingTempService {
    private final LendingTempRepository lendingTempRepository;
    private final LendingRepository lendingRepository;


    public LendingTempService(LendingTempRepository lendingTempRepository, LendingRepository lendingRepository) {
        this.lendingTempRepository = lendingTempRepository;
        this.lendingRepository = lendingRepository;
    }


    public LendingTemp processReturnRequest(ReturnLendingTempRequest resource) {
        // Buscar o empréstimo pelo ID
        Lending lending = lendingRepository.findById(resource.getId())
                .orElseThrow(() -> new NotFoundException("[ERROR] Lending not found"));

        // Verificar se o livro já foi devolvido
        if (lending.isReturned()) {
            throw new IllegalArgumentException("[ERROR] Book with lending number: " + resource.getId() + " is already returned.");
        }


        // Criar LendingTemp
        LendingTemp lendingTemp = new LendingTemp();
        lendingTemp.setId(resource.getId());
        lendingTemp.setLendingCode(lending.getLendingCode());
        lendingTemp.setComment(resource.getComment());
        lendingTemp.setRecom(resource.isRecom());
        lendingTemp.setCom(resource.getCom());

        // Salvar LendingTemp
        return lendingTempRepository.save(lendingTemp);
    }

    public LendingTemp findByLendingCode(String lendingCode) {
        return lendingTempRepository.findFirstByLendingCode(lendingCode);
    }
}
