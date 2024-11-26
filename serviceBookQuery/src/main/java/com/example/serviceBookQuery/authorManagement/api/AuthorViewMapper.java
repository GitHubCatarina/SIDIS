package com.example.serviceBookQuery.authorManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceBookQuery.authorManagement.model.Author;

@Mapper(componentModel = "spring")
public abstract class AuthorViewMapper {
    public abstract AuthorView toAuthorView(Author author);
    public abstract Iterable<AuthorView> toAuthorView(Iterable<Author> author);
}