package com.example.serviceAuth.userManagement.sync;

import com.example.serviceAuth.userManagement.dto.UserDTO;
import com.example.serviceAuth.userManagement.model.User;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyncRequest {
    @JsonProperty
    private Long id; // Id do User
    @JsonProperty
    private UserDTO resource; // Dados para criar ou atualizar User
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
    public SyncRequest() {}

    // Construtor com parâmetros
    public SyncRequest(Long id, UserDTO resource, long desiredVersion, String instanceId) {
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

    public UserDTO getResource() {
        return resource;
    }

    public void setResource(UserDTO resource) {
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