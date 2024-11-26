package com.example.serviceTopQuery.topManagement.services;

import com.example.serviceTopQuery.topManagement.api.LendingReaderView;
import com.example.serviceTopQuery.topManagement.repositories.LendingRepository;
import com.example.serviceTopQuery.topManagement.api.LentBookView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopServiceImpl implements TopService {

    private final LendingRepository LendingRepository;


    @Autowired
    public TopServiceImpl(LendingRepository LendingRepository) {
        this.LendingRepository = LendingRepository;

    }


    @Override
    public List<LentBookView> getTopBooks() {
        List<Object[]> topBookIds = LendingRepository.findTopBookIds();

        return topBookIds.stream()
                .map(record -> new LentBookView((Long) record[0], (Long) record[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingReaderView> getTopReaders() {
        Pageable pageable = PageRequest.of(0, 5); // Define que queremos apenas os 5 primeiros resultados
        List<Object[]> topReaders = LendingRepository.findTopReaders(pageable);

        return topReaders.stream()
                .map(record -> new LendingReaderView((Long) record[0], (Long) record[1]))
                .collect(Collectors.toList());
    }

}
