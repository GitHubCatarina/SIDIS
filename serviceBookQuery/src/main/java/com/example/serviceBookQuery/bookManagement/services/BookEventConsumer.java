package com.example.serviceBookQuery.bookManagement.services;

import com.example.serviceBookQuery.authorManagement.repository.AuthorRepository;
import com.example.serviceBookQuery.bookManagement.model.Book;
import com.example.serviceBookQuery.bookManagement.repositories.BookRepository;
import com.example.serviceBookQuery.bookManagement.repositories.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

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
            Hibernate.initialize(book.getCover());

            // Salvar o livro no repositório
            bookRepository.save(book);
            System.out.println("Livro com ISBN " + resource.getIsbn() + " criado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao processar a mensagem do livro.");
        }
    }
}