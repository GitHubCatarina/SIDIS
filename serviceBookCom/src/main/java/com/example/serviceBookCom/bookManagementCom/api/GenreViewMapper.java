package com.example.serviceBookCom.bookManagementCom.api;

import com.example.serviceBookCom.bookManagementCom.api.GenreView;
import com.example.serviceBookCom.bookManagementCom.model.Book;
import com.example.serviceBookCom.bookManagementCom.model.Genre;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public class GenreViewMapper {
    public GenreView toGenreView(Genre genre, Iterable<Book> books) {
        if ( genre == null ) {
            return null;
        }

        GenreView genreView = new GenreView();

        genreView.setId( genre.getId() );
        genreView.setName( genre.getName());
        List<Book> booksList = new ArrayList<>();
        for (Book book : books) {
            booksList.add(book);
        }
        genreView.setBookCount(countBooksForGenre(genre, booksList));

        return genreView;
    }
    public Iterable<GenreView> toGenreView(Iterable<Genre> genres, Iterable<Book> books) {
        if ( genres == null ) {
            return null;
        }

        ArrayList<GenreView> iterable = new ArrayList<GenreView>();
        for ( Genre genre : genres ) {
            iterable.add( toGenreView(genre, books));
        }

        return iterable;
    }

    private int countBooksForGenre(Genre genre, List<Book> books) {
        int count = 0;
        for (Book book : books) {
            if (book.getGenre().getId().equals(genre.getId())) {
                count++;
            }
        }
        return count;
    }
}
