package com.example.serviceLending.lendingManagement.sync;

import com.example.serviceLending.lendingManagement.model.Lending; // Importe a classe Lending

public class SyncRequest {
    private Long lendingId; // Id do Lending
    private Lending resource; // Dados para criar ou atualizar Lending
    private long desiredVersion; // Para controlar a versão
    private String instanceId; // Para identificar a instância

    @Override
    public String toString() {
        return "SyncRequest{" +
                "lendingId=" + lendingId +
                ", resource=" + resource +
                '}';
    }

    // Construtor padrão
    public SyncRequest() {}

    // Construtor com parâmetros
    public SyncRequest(Long lendingId, Lending resource, long desiredVersion, String instanceId) {
        this.lendingId = lendingId;
        this.resource = resource;
        this.desiredVersion = desiredVersion;
        this.instanceId = instanceId;
    }

    // Getters e Setters
    public Long getLendingId() {
        return lendingId;
    }

    public void setLendingId(Long lendingId) {
        this.lendingId = lendingId;
    }

    public Lending getResource() {
        return resource;
    }

    public void setResource(Lending resource) {
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