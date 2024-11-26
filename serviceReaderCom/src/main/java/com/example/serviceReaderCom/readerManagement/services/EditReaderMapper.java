package com.example.serviceReaderCom.readerManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceReaderCom.readerManagement.model.Reader;

@Mapper(componentModel = "spring")
public abstract class EditReaderMapper {
    public abstract Reader create(EditReaderRequest request);
}
