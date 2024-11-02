package com.example.serviceBook.bookManagement.services;

import org.mapstruct.Mapper;
import com.example.serviceBook.bookManagement.model.Book;
import com.example.serviceBook.bookManagement.model.BookAuthor;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public class EditBookMapper {
    public Book create(CreateBookRequest request) {
        if ( request == null ) {
            return null;
        }

        Book book = new Book();

        book.setIsbn( request.getIsbn() );
        book.setTitle( request.getTitle() );
        book.setGenre( request.getGenre() );

        if ( request.getDescription() == null) {
            book.setDescription(" ");
        } else {
            book.setDescription( request.getDescription());
        }
        List<BookAuthor> list = request.getBookAuthors();
        if ( list != null ) {
            book.setBookAuthors( new ArrayList<BookAuthor>( list ) );
        }

        return book;
    }
}

