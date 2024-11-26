package com.example.serviceBookQuery.authorManagement.dto;

import com.example.serviceBookQuery.authorManagement.model.Author;

public class AuthorDTO {
    private Long id;
    private String name;
    private String shortBio;
    private int lents;
    private Long authorPhotoId;

    public AuthorDTO() {}

    public AuthorDTO(Long id, String name, String shortBio, int lents, Long authorPhotoId) {
        this.id = id;
        this.name = name;
        this.shortBio = shortBio;
        this.lents = lents;
        this.authorPhotoId = authorPhotoId;
    }

    public static AuthorDTO fromEntity(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setShortBio(author.getShortBio());
        dto.setLents(author.getLents());

        if (author.getAuthorPhoto() != null) {
            dto.setAuthorPhotoId(author.getAuthorPhoto().getId());
        }

        return dto;
    }

    public static Author toEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setShortBio(authorDTO.getShortBio());
        author.setLents(authorDTO.getLents());
        return author;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public int getLents() {
        return lents;
    }

    public void setLents(int lents) {
        this.lents = lents;
    }

    public Long getAuthorPhotoId() {
        return authorPhotoId;
    }

    public void setAuthorPhotoId(Long authorPhotoId) {
        this.authorPhotoId = authorPhotoId;
    }

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortBio='" + shortBio + '\'' +
                ", lents=" + lents +
                ", authorPhotoId=" + authorPhotoId +
                '}';
    }
}
