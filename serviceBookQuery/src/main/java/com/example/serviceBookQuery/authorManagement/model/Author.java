package com.example.serviceBookQuery.authorManagement.model;

import jakarta.persistence.*;
import org.hibernate.StaleObjectStateException;

@Entity
@Table
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;
    @Column(length = 150, nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT", length = 4096, nullable = false)
    private String shortBio;

    private int lents;

    @OneToOne
    private AuthorPhoto authorPhoto;

    public Author() {}

    public Author(String name, String shortBio) {
        setName(name);
        setShortBio(shortBio);
    }

    public Author(String name, String shortBio, AuthorPhoto authorPhoto) {
        setName(name);
        setShortBio(shortBio);
        setAuthorPhoto(authorPhoto);
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLents(int lents){
        this.lents = lents;
    }

    public int getLents(){
        return lents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name can't be blank");
        }
        if (name.length() > 150) {
            throw new IllegalArgumentException("Name exceeds the limit of 150 characters");
        }
        this.name = name;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        if (shortBio == null) {
            throw new IllegalArgumentException("Short Bio can't be null");
        }
        if (shortBio.isBlank()) {
            throw new IllegalArgumentException("Short Bio can't be blank");
        }
        if (shortBio.length() > 4096) {
            throw new IllegalArgumentException("Short Bio exceeds the limit of 4096 characters");
        }
        this.shortBio = shortBio;
    }

    public AuthorPhoto getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(AuthorPhoto authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public void applyPatch(final long desiredVersion, final String name, final String shortBio) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if (name != null && !name.isEmpty()) {
            setName(name);
        }
        if (shortBio != null) {
            setShortBio(shortBio);
        }
    }

    public void updateData(final long desiredVersion, final String name, final String shortBio) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        setName(name);
        setShortBio(shortBio);
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", shortBio='" + shortBio + '\'' +
                '}';
    }
}