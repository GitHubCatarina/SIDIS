package com.example.serviceBook.authorManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceBook.authorManagement.model.Author;

@Mapper(componentModel = "spring")
public abstract class EditAuthorMapper {
    public abstract Author create(EditAuthorRequest request);
}