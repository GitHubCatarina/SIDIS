package com.example.serviceBookCom.authorManagementCom.api;

import com.example.serviceBookCom.authorManagementCom.api.AuthorView;
import com.example.serviceBookCom.authorManagementCom.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthorViewMapper {
    public abstract AuthorView toAuthorView(Author author);
    public abstract Iterable<AuthorView> toAuthorView(Iterable<Author> author);
}