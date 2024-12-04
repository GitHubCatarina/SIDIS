package com.example.serviceReaderCom.readerManagement.dto;

import java.time.LocalDate;
import java.util.List;

public class ReaderDTO {
    private Long id;
    private String readerCode;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private Integer age;
    private Integer phoneNumber;
    private Boolean GDBRConsent;
    private List<String> interests;
    private String funnyQuote;

    public ReaderDTO() {
    }

    public ReaderDTO(Long id, String readerCode, String name, String email, LocalDate dateOfBirth, Integer age, Integer phoneNumber, Boolean GDBRConsent, List<String> interests, String funnyQuote) {
        this.id = id;
        this.readerCode = readerCode;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.GDBRConsent = GDBRConsent;
        this.interests = interests;
        this.funnyQuote = funnyQuote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReaderCode() {
        return readerCode;
    }

    public void setReaderCode(String readerCode) {
        this.readerCode = readerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getGDBRConsent() {
        return GDBRConsent;
    }

    public void setGDBRConsent(Boolean GDBRConsent) {
        this.GDBRConsent = GDBRConsent;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getFunnyQuote() {
        return funnyQuote;
    }

    public void setFunnyQuote(String funnyQuote) {
        this.funnyQuote = funnyQuote;
    }

    @Override
    public String toString() {
        return "ReaderDTO{" +
                "id=" + id +
                ", readerCode='" + readerCode + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", GDBRConsent=" + GDBRConsent +
                ", interests=" + interests +
                ", funnyQuote='" + funnyQuote + '\'' +
                '}';
    }
}