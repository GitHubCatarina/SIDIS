package com.example.serviceBookCom.authorManagement.api;

import com.example.serviceBookCom.authorManagement.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthorViewMapper {
    public abstract AuthorView toAuthorView(Author author);
    public abstract Iterable<AuthorView> toAuthorView(Iterable<Author> author);
}