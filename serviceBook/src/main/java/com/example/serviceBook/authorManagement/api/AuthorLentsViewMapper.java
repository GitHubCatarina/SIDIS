package com.example.serviceBook.authorManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceBook.authorManagement.model.Author;

@Mapper(componentModel = "spring")
public abstract class AuthorLentsViewMapper {
    public abstract Iterable<AuthorLentsView> toAuthorLentsView(Iterable<Author> author);
}