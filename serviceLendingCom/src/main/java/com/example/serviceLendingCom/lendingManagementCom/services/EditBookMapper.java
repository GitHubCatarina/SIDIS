package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.lendingManagementCom.model.Book;
import com.example.serviceLendingCom.lendingManagementCom.model.BookAuthor;
import org.mapstruct.Mapper;

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

