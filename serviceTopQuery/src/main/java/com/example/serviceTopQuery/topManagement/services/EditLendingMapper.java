package com.example.serviceTopQuery.topManagement.services;

import com.example.serviceTopQuery.topManagement.model.Lending;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EditLendingMapper {
    public abstract Lending create(CreateLendingRequest request);
}
