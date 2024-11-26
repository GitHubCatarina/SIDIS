package com.example.serviceReaderQuery.readerManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceReaderQuery.readerManagement.model.Reader;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewMapper {
    public abstract ReaderView toReaderView(Reader reader);
    public abstract Iterable<ReaderView> toReaderView(List<Reader> readers);
}
