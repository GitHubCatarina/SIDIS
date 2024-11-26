package com.example.serviceBookQuery.authorManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceBookQuery.authorManagement.model.Author;

@Mapper(componentModel = "spring")
public abstract class AuthorLentsViewMapper {
    public abstract Iterable<AuthorLentsView> toAuthorLentsView(Iterable<Author> author);
}