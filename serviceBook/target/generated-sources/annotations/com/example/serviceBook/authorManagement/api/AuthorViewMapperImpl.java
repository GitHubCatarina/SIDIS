package com.example.serviceBook.authorManagement.api;

import com.example.serviceBook.authorManagement.model.Author;
import java.util.ArrayList;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-02T16:03:28+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class AuthorViewMapperImpl extends AuthorViewMapper {

    @Override
    public AuthorView toAuthorView(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorView authorView = new AuthorView();

        authorView.setId( author.getId() );
        authorView.setName( author.getName() );
        authorView.setShortBio( author.getShortBio() );

        return authorView;
    }

    @Override
    public Iterable<AuthorView> toAuthorView(Iterable<Author> author) {
        if ( author == null ) {
            return null;
        }

        ArrayList<AuthorView> iterable = new ArrayList<AuthorView>();
        for ( Author author1 : author ) {
            iterable.add( toAuthorView( author1 ) );
        }

        return iterable;
    }
}
