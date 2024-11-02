package com.example.serviceReader.readerManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceReader.readerManagement.model.Reader;

@Mapper(componentModel = "spring")
public abstract class ReaderLentsViewMapper {
    public abstract Iterable<ReaderLentsView> toReaderLentsView (Iterable<Reader> readers);
}
