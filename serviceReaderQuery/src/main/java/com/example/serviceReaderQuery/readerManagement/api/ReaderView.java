package com.example.serviceReaderQuery.readerManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Reader")
public class ReaderView {
    private Long id;
    private String readerCode;
    private String name;
    private String email;
    private Integer age;
    private Integer phoneNumber;
    private Boolean GDBRConsent;
    private List<String> interests;
}

