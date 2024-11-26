package com.example.serviceBookCom.bookManagement.api;

import com.example.serviceBookCom.bookManagement.model.BookAuthor;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public class BookAuthorViewMapper {
    public static BookAuthorView toBookAuthorView(BookAuthor bookAuthor) {
        if ( bookAuthor == null ) {
            return null;
        }

        BookAuthorView bookAuthorView = new BookAuthorView();

        bookAuthorView.setAuthor( bookAuthor.getAuthor().getName());
        bookAuthorView.setShortBio( bookAuthor.getAuthor().getShortBio());

        return bookAuthorView;
    }

    public static Iterable<BookAuthorView> toBookAuthorView(Iterable<BookAuthor> bookAuthors) {
        if ( bookAuthors == null ) {
            return null;
        }

        ArrayList<BookAuthorView> iterable = new ArrayList<>();
        for ( BookAuthor bookAuthor : bookAuthors ) {
            iterable.add( toBookAuthorView( bookAuthor ) );
        }

        return iterable;
    }
}
