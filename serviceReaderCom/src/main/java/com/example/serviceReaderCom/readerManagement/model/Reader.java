package com.example.serviceReaderCom.readerManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.StaleObjectStateException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;


@Entity
@Table
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 255)
    private String readerCode;

    @Version
    private long version;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private Integer age;

    private int lents;

    @Column(nullable = false)
    private Integer phoneNumber;

    @Column(nullable = false)
    private Boolean GDBRConsent;

    @ElementCollection
    @CollectionTable(name = "reader_interests", joinColumns = @JoinColumn(name = "reader_id"))
    @Column(name = "interest", nullable = true)
    private List<String> interests;

    @OneToOne(cascade = CascadeType.ALL)
    private ReaderPhoto readerPhoto;

    private String funnyQuote;

    private Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return null;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public Reader() {

    }

    public Reader(String readerCode, String name, String email, LocalDate dateOfBirth, Integer phoneNumber, Boolean GDBRConsent, List<String> interests, String funnyQuote) {
        this.readerCode = readerCode;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.GDBRConsent = GDBRConsent;
        this.interests = interests;
        this.funnyQuote = funnyQuote;
    }

    public Reader(String readerCode, String name, String email, LocalDate dateOfBirth, Integer phoneNumber, Boolean GDBRConsent, List<String> interests) {
        this.readerCode = readerCode;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        this.age = period.getYears();
        this.phoneNumber = phoneNumber;
        this.GDBRConsent = GDBRConsent;
        this.interests = interests;
    }

    public Reader(String readerCode, String name, String email, LocalDate dateOfBirth, Integer phoneNumber, Boolean GDBRConsent, List<String> interests,ReaderPhoto readerPhoto) {
        this.readerCode = readerCode;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        this.age = period.getYears();
        this.phoneNumber = phoneNumber;
        this.GDBRConsent = GDBRConsent;
        this.interests = interests;
        this.readerPhoto = readerPhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getReaderCode() {
        return readerCode;
    }

    public void setReaderCode(@NotNull String readerCode) {
        this.readerCode = readerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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
        updateAge();
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getLents() {
        return lents;
    }

    public void setLents(int lents) {
        this.lents = lents;
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

    public ReaderPhoto getReaderPhoto() {
        return readerPhoto;
    }

    public void setReaderPhoto(ReaderPhoto readerPhoto) {
        this.readerPhoto = readerPhoto;
    }

    public String getFunnyQuote() {
        return funnyQuote;
    }

    public void setFunnyQuote(String funnyQuote) {
        this.funnyQuote = funnyQuote;
    }

    public void updateData(final long desiredVersion, final String name, final String email, final LocalDate dateOfBirth, final Integer phoneNumber, final Boolean GDBRConsent, final List<String> interests) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        setName(name);
        setEmail(email);
        setDateOfBirth(dateOfBirth);
        setPhoneNumber(phoneNumber);
        setGDBRConsent(GDBRConsent);
        setInterests(interests);
    }

    public void applyPatch(final long desiredVersion, final String name, final String email, final LocalDate dateOfBirth, final Integer phoneNumber, final Boolean GDBRConsent, final List<String> interests) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if (name != null && !name.isEmpty()) {
            setName(name);
        }
        if (email != null) {
            setEmail(email);
        }
        if (dateOfBirth != null) {
            setDateOfBirth(dateOfBirth);
        }
        if (phoneNumber != null) {
            setPhoneNumber(phoneNumber);
        }
        if (GDBRConsent != null) {
            setGDBRConsent(GDBRConsent);
        }
        if (interests != null) {
            setInterests(interests);
        }
    }

    private void updateAge() {
        if (this.dateOfBirth != null) {
            this.age = Period.between(this.dateOfBirth, LocalDate.now()).getYears();
        }
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", readerCode='" + readerCode + '\'' +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", GDBRConsent=" + GDBRConsent +
                ", interests=" + interests +
                '}';
    }
}
