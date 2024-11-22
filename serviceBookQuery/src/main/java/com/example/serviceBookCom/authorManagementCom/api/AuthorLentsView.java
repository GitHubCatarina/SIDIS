package com.example.serviceBookCom.authorManagementCom.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "An Author with is lents")
public class AuthorLentsView {
    private Long id;
    private String name;
    private String shortBio;
    private int lents;
}