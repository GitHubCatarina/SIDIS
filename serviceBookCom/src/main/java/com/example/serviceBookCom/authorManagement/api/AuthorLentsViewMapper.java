package com.example.serviceBookCom.authorManagement.api;

import com.example.serviceBookCom.authorManagement.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthorLentsViewMapper {
    public abstract Iterable<AuthorLentsView> toAuthorLentsView(Iterable<Author> author);
}