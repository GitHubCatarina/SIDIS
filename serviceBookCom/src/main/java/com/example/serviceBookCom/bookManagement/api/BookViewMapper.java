package com.example.serviceBookCom.bookManagement.api;

import com.example.serviceBookCom.bookManagement.model.Book;
import com.example.serviceBookCom.bookManagement.model.BookAuthor;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.example.serviceBookCom.bookManagement.api.BookAuthorViewMapper.toBookAuthorView;
import static com.example.serviceBookCom.bookManagement.api.BookGenreViewMapper.toBookGenreView;

@Mapper(componentModel = "spring")
public class BookViewMapper {
    public BookView toBookView(Book book) {
        if ( book == null ) {
            return null;
        }

        BookView bookView = new BookView();

        bookView.setId( book.getId() );
        bookView.setIsbn( book.getIsbn() );
        bookView.setTitle( book.getTitle() );
        bookView.setGenre( toBookGenreView(book.getGenre()) );
        bookView.setDescription( book.getDescription() );
        List<BookAuthor> list = book.getBookAuthors();
        if (list != null) {
            List<BookAuthorView> viewList = new ArrayList<>();
            for (BookAuthor bookAuthor : list) {
                viewList.add(toBookAuthorView(bookAuthor));
            }
            bookView.setBookAuthors(viewList);
        }

        return bookView;
    }

    public Iterable<BookView> toBookView(Iterable<Book> books) {
        if ( books == null ) {
            return null;
        }

        ArrayList<BookView> iterable = new ArrayList<BookView>();
        for ( Book book : books ) {
            iterable.add( toBookView( book ) );
        }

        return iterable;
    }

    public BookView toCreateBookView(Book book) {
        if ( book == null ) {
            return null;
        }

        BookView bookView = new BookView();

        bookView.setId( book.getId() );
        bookView.setIsbn( book.getIsbn() );
        bookView.setTitle( book.getTitle() );
        bookView.setGenre( toBookGenreView(book.getGenre()) );
        bookView.setDescription( book.getDescription() );
        bookView.setBookAuthors(new ArrayList<>());

        return bookView;
    }
}
