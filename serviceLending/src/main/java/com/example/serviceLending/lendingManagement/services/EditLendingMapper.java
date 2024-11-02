package com.example.serviceLending.lendingManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceLending.lendingManagement.model.Lending;

@Mapper(componentModel = "spring")
public abstract class EditLendingMapper {
    public abstract Lending create(CreateLendingRequest request);
}
