package com.example.serviceBook.authorManagement.services;

import com.example.serviceBook.authorManagement.model.Author;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-02T16:03:27+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class EditAuthorMapperImpl extends EditAuthorMapper {

    @Override
    public Author create(EditAuthorRequest request) {
        if ( request == null ) {
            return null;
        }

        Author author = new Author();

        author.setName( request.getName() );
        author.setShortBio( request.getShortBio() );

        return author;
    }
}
