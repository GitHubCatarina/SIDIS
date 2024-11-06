package com.example.serviceAuth.userManagement.dto;

import com.example.serviceAuth.userManagement.model.User;
import org.springframework.security.core.GrantedAuthority;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private boolean enabled;
    private String username;
    private String password;
    private String fullName;
    private Set<String> authorities;

    // Construtores
    public UserDTO() {}

    public UserDTO(Long id, Long version, LocalDateTime createdAt, LocalDateTime modifiedAt,
                   String createdBy, String modifiedBy, boolean enabled, String username,
                   String password, String fullName, Set<String> authorities) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.enabled = enabled;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.authorities = authorities;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getVersion(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getCreatedBy(),
                user.getModifiedBy(),
                user.isEnabled(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())  // Corrigido para usar getAuthority()
        );
    }

}