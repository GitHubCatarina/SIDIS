package com.example.serviceBookCom.authorManagementCom.services;

import com.example.serviceBookCom.authorManagementCom.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EditAuthorMapper {
    public abstract Author create(EditAuthorRequest request);
}