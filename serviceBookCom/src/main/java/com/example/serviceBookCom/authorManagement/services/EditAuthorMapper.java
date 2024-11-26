package com.example.serviceBookCom.authorManagement.services;

import com.example.serviceBookCom.authorManagement.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EditAuthorMapper {
    public abstract Author create(EditAuthorRequest request);
}