package com.example.serviceReader.readerManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceReader.readerManagement.model.Reader;

@Mapper(componentModel = "spring")
public abstract class EditReaderMapper {
    public abstract Reader create(EditReaderRequest request);
}
