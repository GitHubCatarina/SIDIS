package com.example.serviceBookCom.authorManagementCom.syncAuthor;

import com.example.serviceBook.authorManagement.dto.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorSyncRequest {
    @JsonProperty
    private Long id; // Id do Author
    @JsonProperty
    private AuthorDTO resource; // Dados para criar ou atualizar Author
    @JsonProperty
    private long desiredVersion; // Para controlar a versão
    @JsonProperty
    private String instanceId; // Para identificar a instância

    @Override
    public String toString() {
        return "SyncRequest{" +
                "id=" + id +
                ", resource=" + resource +
                ", desiredVersion=" + desiredVersion +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }

    // Construtor padrão
    public AuthorSyncRequest() {}

    // Construtor com parâmetros
    public AuthorSyncRequest(Long id, AuthorDTO resource, long desiredVersion, String instanceId) {
        this.id = id;
        this.resource = resource;
        this.desiredVersion = desiredVersion;
        this.instanceId = instanceId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorDTO getResource() {
        return resource;
    }

    public void setResource(AuthorDTO resource) {
        this.resource = resource;
    }

    public long getDesiredVersion() {
        return desiredVersion;
    }

    public void setDesiredVersion(long desiredVersion) {
        this.desiredVersion = desiredVersion;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}