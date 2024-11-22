package com.example.serviceBookCom.bookManagementCom.api;

import com.example.serviceBookCom.bookManagementCom.api.BookGenreView;
import com.example.serviceBookCom.bookManagementCom.model.Genre;

import java.util.ArrayList;

public class BookGenreViewMapper {
    public static BookGenreView toBookGenreView(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        BookGenreView bookGenreView = new BookGenreView();

        bookGenreView.setName( genre.getName());

        return bookGenreView;
    }

    public static Iterable<BookGenreView> toBookGenreView(Iterable<Genre> genres) {
        if ( genres == null ) {
            return null;
        }

        ArrayList<BookGenreView> iterable = new ArrayList<>();
        for ( Genre genre : genres ) {
            iterable.add( toBookGenreView( genre ) );
        }

        return iterable;
    }
}
