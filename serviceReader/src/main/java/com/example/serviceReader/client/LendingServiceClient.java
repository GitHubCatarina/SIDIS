// LendingServiceClient
package com.example.serviceReader.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.serviceReader.readerManagement.api.LendingReaderView;

import java.util.Arrays;
import java.util.List;

@Component
public class LendingServiceClient {

    private final RestTemplate restTemplate;

    @Autowired
    public LendingServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LendingReaderView> getTopReaders() {
        String url = "http://localhost:8083/api/lendings/top-readers";
        ResponseEntity<LendingReaderView[]> response = restTemplate.getForEntity(url, LendingReaderView[].class);
        return Arrays.asList(response.getBody());
    }
}
