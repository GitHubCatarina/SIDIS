package com.example.serviceBookCom.bookManagement.services;

import com.example.serviceBookCom.bookManagement.model.Book;
import com.example.serviceBookCom.bookManagement.repositories.BookRepository;
import com.example.serviceBookCom.bookManagement.dto.SyncBookDTO;
import com.example.serviceBookCom.bookManagement.model.BookCover;
import com.example.serviceBookCom.bookManagement.model.Genre;
import com.example.serviceBookCom.bookManagement.repositories.GenreRepository;
import com.example.serviceBookCom.authorManagement.model.Author;
import com.example.serviceBookCom.authorManagement.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


import java.util.Base64;
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

    @RabbitListener(queues = "#{bookQueue.name}", ackMode = "AUTO")
    public void handleBookCreatedEvent(BookEventMessage bookEventMessage) {
        System.out.println("Mensagem recebida para livro com ID: " + bookEventMessage.getBookJson());

        try {
            // Desserializa o JSON para o objeto CreateBookRequest
            CreateBookRequest resource = new ObjectMapper().readValue(bookEventMessage.getBookJson(), CreateBookRequest.class);

            // Verificar se o livro já existe
            boolean bookExists = bookRepository.existsByIsbn(resource.getIsbn());

            if (bookExists) {
                System.out.println("Livro com ISBN " + resource.getIsbn() + " já existe, não será criado.");
                return;
            }

            // Criar o livro utilizando o método do BookService
            Book book = bookService.createBook(resource, bookEventMessage.getCoverPhoto() != null ? new MultipartFileAdapter(bookEventMessage.getCoverPhoto()) : null);

            // Inicializa as associações para evitar LazyInitializationException
            Hibernate.initialize(book.getBookAuthors());

            // Salvar o livro no repositório
            bookRepository.save(book);
            System.out.println("Livro com ISBN " + resource.getIsbn() + " criado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao processar a mensagem do livro.");
        }
    }
}