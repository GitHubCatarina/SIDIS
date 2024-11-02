package com.example.serviceReader.readerManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceReader.readerManagement.model.Reader;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewMapper {
    public abstract ReaderView toReaderView(Reader reader);
    public abstract Iterable<ReaderView> toReaderView(List<Reader> readers);
}
