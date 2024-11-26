package com.example.serviceReaderCom.readerManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceReaderCom.readerManagement.model.Reader;

@Mapper(componentModel = "spring")
public abstract class ReaderLentsViewMapper {
    public abstract Iterable<ReaderLentsView> toReaderLentsView (Iterable<Reader> readers);
}
