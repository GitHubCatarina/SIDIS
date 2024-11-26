package com.example.serviceReaderCom.readerManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceReaderCom.readerManagement.model.Reader;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewMapper {
    public abstract ReaderView toReaderView(Reader reader);
    public abstract Iterable<ReaderView> toReaderView(List<Reader> readers);
}
