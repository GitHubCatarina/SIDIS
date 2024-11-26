package com.example.serviceReaderQuery.readerManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceReaderQuery.readerManagement.model.Reader;

@Mapper(componentModel = "spring")
public abstract class EditReaderMapper {
    public abstract Reader create(EditReaderRequest request);
}
