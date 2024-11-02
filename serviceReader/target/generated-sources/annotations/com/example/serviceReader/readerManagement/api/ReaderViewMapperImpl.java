package com.example.serviceReader.readerManagement.api;

import com.example.serviceReader.readerManagement.model.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-02T16:18:32+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class ReaderViewMapperImpl extends ReaderViewMapper {

    @Override
    public ReaderView toReaderView(Reader reader) {
        if ( reader == null ) {
            return null;
        }

        ReaderView readerView = new ReaderView();

        readerView.setId( reader.getId() );
        readerView.setReaderCode( reader.getReaderCode() );
        readerView.setName( reader.getName() );
        readerView.setEmail( reader.getEmail() );
        readerView.setAge( reader.getAge() );
        readerView.setPhoneNumber( reader.getPhoneNumber() );
        readerView.setGDBRConsent( reader.getGDBRConsent() );
        List<String> list = reader.getInterests();
        if ( list != null ) {
            readerView.setInterests( new ArrayList<String>( list ) );
        }

        return readerView;
    }

    @Override
    public Iterable<ReaderView> toReaderView(List<Reader> readers) {
        if ( readers == null ) {
            return null;
        }

        ArrayList<ReaderView> iterable = new ArrayList<ReaderView>( readers.size() );
        for ( Reader reader : readers ) {
            iterable.add( toReaderView( reader ) );
        }

        return iterable;
    }
}
