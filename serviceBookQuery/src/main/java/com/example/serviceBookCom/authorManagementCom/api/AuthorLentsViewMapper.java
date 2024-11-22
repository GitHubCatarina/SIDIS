package com.example.serviceBookCom.authorManagementCom.api;

import com.example.serviceBookCom.authorManagementCom.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthorLentsViewMapper {
    public abstract Iterable<AuthorLentsView> toAuthorLentsView(Iterable<Author> author);
}