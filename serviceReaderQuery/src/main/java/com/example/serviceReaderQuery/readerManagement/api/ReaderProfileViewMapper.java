package com.example.serviceReaderQuery.readerManagement.api;

import org.mapstruct.Mapper;
import com.example.serviceReaderQuery.readerManagement.model.Reader;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public class ReaderProfileViewMapper {
    public ReaderProfileView toReaderProfileView(Reader reader) {
        if ( reader == null ) {
            return null;
        }

        ReaderProfileView readerProfileView = new ReaderProfileView();

        readerProfileView.setId( reader.getId() );
        readerProfileView.setReaderCode( reader.getReaderCode() );
        readerProfileView.setName( reader.getName() );
        readerProfileView.setEmail( reader.getEmail() );
        readerProfileView.setAge( reader.getAge() );
        readerProfileView.setPhoneNumber( reader.getPhoneNumber() );
        readerProfileView.setGDBRConsent( reader.getGDBRConsent() );
        readerProfileView.setFunnyQuote(reader.getFunnyQuote());
        List<String> list = reader.getInterests();
        if ( list != null ) {
            readerProfileView.setInterests( new ArrayList<String>( list ) );
        }

        return readerProfileView;
    }

}
