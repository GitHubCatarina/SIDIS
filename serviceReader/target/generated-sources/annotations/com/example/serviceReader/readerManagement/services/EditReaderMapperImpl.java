package com.example.serviceReader.readerManagement.services;

import com.example.serviceReader.readerManagement.model.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-03T17:21:27+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class EditReaderMapperImpl extends EditReaderMapper {

    @Override
    public Reader create(EditReaderRequest request) {
        if ( request == null ) {
            return null;
        }

        Reader reader = new Reader();

        reader.setName( request.getName() );
        reader.setEmail( request.getEmail() );
        reader.setDateOfBirth( request.getDateOfBirth() );
        reader.setPhoneNumber( request.getPhoneNumber() );
        reader.setGDBRConsent( request.getGDBRConsent() );
        List<String> list = request.getInterests();
        if ( list != null ) {
            reader.setInterests( new ArrayList<String>( list ) );
        }

        return reader;
    }
}
