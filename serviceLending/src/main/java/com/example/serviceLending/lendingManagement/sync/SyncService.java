package com.example.serviceLending.lendingManagement.sync;

import com.example.serviceLending.lendingManagement.api.LendingViewMapper;
import com.example.serviceLending.lendingManagement.model.Lending;
import com.example.serviceLending.lendingManagement.repositories.LendingRepository;
import com.example.serviceLending.lendingManagement.services.EditLendingRequest;
import com.example.serviceLending.lendingManagement.services.LendingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class SyncService {

    private final LendingRepository lendingRepository;

    public SyncService(LendingRepository lendingRepository) {
        this.lendingRepository = lendingRepository;
    }

    @Transactional
    public void syncData(SyncRequest syncRequest) {
        System.out.println("Sync Request: " + syncRequest);

        Long lendingId = syncRequest.getLendingId();
        if (lendingId == null) {
            throw new IllegalArgumentException("LendingID não pode ser nulo");
        }

        // Procurar o recurso pelo lendingId fornecido no SyncRequest
        Lending existingLending = lendingRepository.findById(lendingId).orElse(null);

        if (existingLending != null) {
            // Atualiza o recurso existente com os dados do EditLendingRequest
            updateLending(existingLending, syncRequest.getResource());
            System.out.println("Recurso atualizado com sucesso.");
        } else {
            // Cria novo recurso com base nos dados do EditLendingRequest
            createLending(syncRequest);
            System.out.println("Recurso criado com sucesso.");
        }
    }

    private void updateLending(Lending existingLending, Lending resource) {
        // Atualiza os campos do objeto Lending existente com base no Lending recebido, se não forem nulos
        if (resource.getLendingCode() != null) {
            existingLending.setLendingCode(resource.getLendingCode());
        }
        if (resource.getReaderId() != null) {
            existingLending.setReaderId(resource.getReaderId());
        }
        if (resource.getBookId() != null) {
            existingLending.setBookId(resource.getBookId());
        }
        if (resource.getBookTitle() != null) {
            existingLending.setBookTitle(resource.getBookTitle());
        }
        if (resource.getLendDate() != null) {
            existingLending.setLendDate(resource.getLendDate());
        }
        if (resource.getLimitDate() != null) {
            existingLending.setLimitDate(resource.getLimitDate());
        }
        if (resource.getReturnedDate() != null) {
            existingLending.setReturnedDate(resource.getReturnedDate());
        }
        if (resource.getDaysTillReturn() != null) {
            existingLending.setDaysTillReturn(resource.getDaysTillReturn());
        }
        existingLending.setReturned(resource.isReturned());
        if (resource.getDaysOverdue() != null) {
            existingLending.setDaysOverdue(resource.getDaysOverdue());
        }
        if (resource.getFine() != null) {
            existingLending.setFine(resource.getFine());
        }
        if (resource.getComment() != null) {
            existingLending.setComment(resource.getComment());
        }

        lendingRepository.save(existingLending);
    }



    private void createLending(SyncRequest syncRequest) {
        // Cria um novo objeto Lending e preenche com dados do EditLendingRequest
        Lending newLending = new Lending();
        newLending.setId(syncRequest.getLendingId());
        newLending.setLendingCode(syncRequest.getResource().getLendingCode());
        newLending.setReaderId(syncRequest.getResource().getReaderId());
        newLending.setBookId(syncRequest.getResource().getBookId());
        newLending.setBookTitle(syncRequest.getResource().getBookTitle());
        newLending.setLendDate(syncRequest.getResource().getLendDate());
        newLending.setLimitDate(syncRequest.getResource().getLimitDate());
        newLending.setReturnedDate(syncRequest.getResource().getReturnedDate());
        newLending.setDaysTillReturn(syncRequest.getResource().getDaysTillReturn());
        newLending.setReturned(syncRequest.getResource().isReturned());
        newLending.setDaysOverdue(syncRequest.getResource().getDaysOverdue());
        newLending.setFine(syncRequest.getResource().getFine());
        newLending.setComment(syncRequest.getResource().getComment());

        lendingRepository.save(newLending);
    }
}
