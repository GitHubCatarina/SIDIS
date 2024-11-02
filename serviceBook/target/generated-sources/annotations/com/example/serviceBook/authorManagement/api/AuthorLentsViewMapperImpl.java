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
public class AuthorLentsViewMapperImpl extends AuthorLentsViewMapper {

    @Override
    public Iterable<AuthorLentsView> toAuthorLentsView(Iterable<Author> author) {
        if ( author == null ) {
            return null;
        }

        ArrayList<AuthorLentsView> iterable = new ArrayList<AuthorLentsView>();
        for ( Author author1 : author ) {
            iterable.add( authorToAuthorLentsView( author1 ) );
        }

        return iterable;
    }

    protected AuthorLentsView authorToAuthorLentsView(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorLentsView authorLentsView = new AuthorLentsView();

        authorLentsView.setId( author.getId() );
        authorLentsView.setName( author.getName() );
        authorLentsView.setShortBio( author.getShortBio() );
        authorLentsView.setLents( author.getLents() );

        return authorLentsView;
    }
}
