package com.example.serviceLendingQuery.lendingManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceLendingQuery.lendingManagement.model.Lending;

@Mapper(componentModel = "spring")
public abstract class EditLendingMapper {
    public abstract Lending create(CreateLendingRequest request);
}
