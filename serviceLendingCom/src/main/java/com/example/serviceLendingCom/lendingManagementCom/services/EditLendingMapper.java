package com.example.serviceLendingCom.lendingManagementCom.services;

import org.mapstruct.Mapper;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;

@Mapper(componentModel = "spring")
public abstract class EditLendingMapper {
    public abstract Lending create(CreateLendingRequest request);
}
