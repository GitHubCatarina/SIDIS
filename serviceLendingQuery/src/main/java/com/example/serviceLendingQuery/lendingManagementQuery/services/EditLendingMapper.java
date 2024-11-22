package com.example.serviceLendingQuery.lendingManagementQuery.services;

import com.example.serviceLendingQuery.lendingManagementQuery.model.Lending;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EditLendingMapper {
    public abstract Lending create(CreateLendingRequest request);
}
