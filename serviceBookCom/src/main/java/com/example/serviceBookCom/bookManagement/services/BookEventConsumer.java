package com.example.serviceBookCom.bookManagement.services;

import com.example.serviceBookCom.authorManagement.model.Author;
import com.example.serviceBookCom.authorManagement.repository.AuthorRepository;
import com.example.serviceBookCom.bookManagement.dto.SyncBookDTO;
import com.example.serviceBookCom.bookManagement.model.Book;
import com.example.serviceBookCom.bookManagement.model.BookAuthor;
import com.example.serviceBookCom.bookManagement.model.BookCover;
import com.example.serviceBookCom.bookManagement.model.Genre;
import com.example.serviceBookCom.bookManagement.repositories.BookRepository;
import com.example.serviceBookCom.bookManagement.repositories.GenreRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookEventConsumer {

    private final BookRepository bookRepository;
    private final BookService bookService;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    public BookEventConsumer(BookServiceImpl bookService, BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    // Consumidor para a fila principal de sincronização
    @RabbitListener(queues = "#{bookQueue.name}", ackMode = "AUTO")
    public void handleBookCreatedEvent(SyncBookDTO bookDTO) {
        System.out.println("Mensagem recebida para livro com ID: " + bookDTO.getId());

        // Verificar se o livro já existe
        boolean bookExists = bookService.bookExists(bookDTO.getId());

        if (bookExists) {
            System.out.println("Livro com ID " + bookDTO.getId() + " já existe, não será criado.");
            return;
        }

        // Recriar o objeto Book
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());

        // Associar o género pelo nome (ou “ID”, dependendo da sua lógica)
        Genre genre = genreRepository.findGenreById(bookDTO.getGenreId())
                .orElseThrow(() -> new IllegalArgumentException("Género não encontrado: " + bookDTO.getGenreId()));
        book.setGenre(genre);

        // Adicionar autores
        List<BookAuthor> bookAuthors = bookDTO.getBookAuthors().stream()
                .map(authorName -> {
                    Author author = authorRepository.findAuthorByName(authorName)
                            .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado: " + authorName));
                    return new BookAuthor(book, author);
                }).collect(Collectors.toList());
        book.setBookAuthors(bookAuthors);

        // Adicionar a capa, se presente
        if (bookDTO.getCover() != null) {
            BookCover cover = new BookCover();
            cover.setCoverUrl(bookDTO.getCover().getCoverUrl());
            cover.setContentType(bookDTO.getCover().getContentType());
            book.setCover(cover);
        }

        bookRepository.save(book);
        System.out.println("Livro com ID " + bookDTO.getId() + " criado com sucesso.");
    }

}