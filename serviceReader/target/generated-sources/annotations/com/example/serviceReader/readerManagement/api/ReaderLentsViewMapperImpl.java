package com.example.serviceReader.readerManagement.api;

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
public class ReaderLentsViewMapperImpl extends ReaderLentsViewMapper {

    @Override
    public Iterable<ReaderLentsView> toReaderLentsView(Iterable<Reader> readers) {
        if ( readers == null ) {
            return null;
        }

        ArrayList<ReaderLentsView> iterable = new ArrayList<ReaderLentsView>();
        for ( Reader reader : readers ) {
            iterable.add( readerToReaderLentsView( reader ) );
        }

        return iterable;
    }

    protected ReaderLentsView readerToReaderLentsView(Reader reader) {
        if ( reader == null ) {
            return null;
        }

        ReaderLentsView readerLentsView = new ReaderLentsView();

        readerLentsView.setId( reader.getId() );
        readerLentsView.setReaderCode( reader.getReaderCode() );
        readerLentsView.setName( reader.getName() );
        readerLentsView.setEmail( reader.getEmail() );
        readerLentsView.setAge( reader.getAge() );
        readerLentsView.setPhoneNumber( reader.getPhoneNumber() );
        readerLentsView.setGDBRConsent( reader.getGDBRConsent() );
        List<String> list = reader.getInterests();
        if ( list != null ) {
            readerLentsView.setInterests( new ArrayList<String>( list ) );
        }
        readerLentsView.setLents( reader.getLents() );

        return readerLentsView;
    }
}
