package com.example.serviceBookQuery.authorManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceBookQuery.authorManagement.model.Author;

@Mapper(componentModel = "spring")
public abstract class EditAuthorMapper {
    public abstract Author create(EditAuthorRequest request);
}