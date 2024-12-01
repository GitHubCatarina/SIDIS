package com.example.serviceBookCom.bookManagement.services;

import com.example.serviceBookCom.authorManagement.model.Author;
import com.example.serviceBookCom.authorManagement.repository.AuthorRepository;
import com.example.serviceBookCom.bookManagement.model.Book;
import com.example.serviceBookCom.bookManagement.model.BookAuthor;
import com.example.serviceBookCom.bookManagement.model.BookCover;
import com.example.serviceBookCom.bookManagement.model.Genre;
import com.example.serviceBookCom.bookManagement.repositories.BookAuthorRepository;
import com.example.serviceBookCom.bookManagement.repositories.BookCoverRepository;
import com.example.serviceBookCom.bookManagement.repositories.BookRepository;
import com.example.serviceBookCom.bookManagement.repositories.GenreRepository;
import com.example.serviceBookCom.bookManagement.util.BookUtil;
import com.example.serviceBookCom.exceptions.NotFoundException;
import com.example.serviceBookCom.fileStorage.FileStorageService;
import com.example.serviceBookCom.fileStorage.UploadFileResponse;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.serviceBookCom.bookManagement.util.BookUtil.*;

@Service
public class BookServiceImpl implements BookService {
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final BookCoverRepository bookCoverRepository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final GenreRepository genreRepository;
    private final FileStorageService fileStorageService;
    private final EditBookMapper editBookMapper;


    @Autowired
    public BookServiceImpl(BookRepository bookRepository, /*LendingRepository lendingRepository,*/ BookCoverRepository bookCoverRepository, AuthorRepository authorRepository, BookAuthorRepository bookAuthorRepository, EditBookMapper editBookMapper, GenreRepository genreRepository, FileStorageService fileStorageService, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.bookCoverRepository = bookCoverRepository;
        this.authorRepository = authorRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.editBookMapper = editBookMapper;
        this.genreRepository = genreRepository;
        this.fileStorageService =  fileStorageService;
        this.restTemplate = restTemplate;
    }

    public Optional<Book> getBook(final String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Iterable<Genre> getTopGenres() {
        return bookRepository.findTopGenres();
    }

    /*
    public List<LentBookView> getTopBooks() {

        return lentBooks.stream().map(lentBook -> {
            //System.out.println("Book ID: " + lentBook.getBookId());
            Book book = bookRepository.findById(lentBook.getBookId()).orElse(null);
            if (book != null) {
                LentBookView lentBookView = new LentBookView();
                lentBookView.setBookId(book.getId());
                lentBookView.setIsbn(book.getIsbn());
                lentBookView.setTitle(book.getTitle());
                lentBookView.setDescription(book.getDescription());

                BookGenreView genreView = new BookGenreView();
                genreView.setName(book.getGenre().getName());
                lentBookView.setGenre(genreView);

                List<AuthorView> authorViews = book.getBookAuthors().stream()
                        .map(bookAuthor -> {
                            AuthorView authorView = new AuthorView();
                            authorView.setId(bookAuthor.getAuthor().getId());
                            authorView.setName(bookAuthor.getAuthor().getName());
                            return authorView;
                        }).collect(Collectors.toList());
                lentBookView.setBookAuthors(authorViews);

                return lentBookView;
            }
            return null;
        }).collect(Collectors.toList());
    }
*/

    public Page<Book> getBooksByGenre(final String genre, Pageable pageable) {
        List<Book> filteredBooks = bookRepository.findAll().stream()
                .filter(book -> book.getGenre().getName().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
        return toPage(filteredBooks, pageable);
    }

    public Page<Book> getBooksByTitle(final String title, Pageable pageable) {
        List<Book> filteredBooks = bookRepository.findAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        return toPage(filteredBooks, pageable);
    }

    public Page<Book> getBooksByAuthor(final String author, Pageable pageable) {
        List<Book> filteredBooks = bookRepository.findAll().stream()
                .filter(book -> book.getBookAuthors().stream()
                        .anyMatch(bookAuthor -> bookAuthor.getAuthor().getName().equalsIgnoreCase(author)))
                .collect(Collectors.toList());
        return toPage(filteredBooks, pageable);
    }

    public Page<Book> getBooksByTitleAndGenreAndAuthor(final String genre, final String title, final String author, Pageable pageable) {
        List<Book> filteredBooks = bookRepository.findAll().stream()
                .filter(book -> book.getGenre().getName().toLowerCase().contains(genre.toLowerCase()) &&
                        book.getTitle().toLowerCase().contains(title.toLowerCase()) &&
                        book.getBookAuthors().stream()
                                .anyMatch(bookAuthor -> bookAuthor.getAuthor().getName().equalsIgnoreCase(author)))
                .collect(Collectors.toList());
        return toPage(filteredBooks, pageable);
    }

    public List<BookAuthor> getBookAuthorsByAuthorId(Long authorId) {
        return bookAuthorRepository.getAuthorBooks(authorId);
    }

    public BookCover getBookCover(final String bookId) {
        final var existingBook = bookRepository.findById(Long.parseLong(bookId)).orElseThrow(() -> new NotFoundException("[ERROR] Book not found"));

        if (existingBook.getCover() == null) {
            throw new IllegalArgumentException("[ERROR] Book Cover not found with ID: " + existingBook.getId());
        }

        return existingBook.getCover();
    }

    public Book createBook(final CreateBookRequest resource, MultipartFile coverPhoto) {
        validateCreateBookRequest(resource);

        Book book = editBookMapper.create(resource);

        if (resource.getBookAuthors() != null) {
            updateBookAuthors(book, resource.getBookAuthors());
        }

        if (coverPhoto != null) {
            doUploadFile(book.getId().toString(), coverPhoto);
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(final Long id, final EditBookRequest resource, final long desiredVersion) {
        final var book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("[ERROR] Cannot update an object that does not yet exist"));

        final var existingGenre = genreRepository.findById(resource.getGenre().getId()).orElseThrow(() -> new NotFoundException("[ERROR] Genre not found"));

        book.updateData(desiredVersion, resource.getTitle(), existingGenre ,resource.getDescription());

        if (resource.getBookAuthors() != null) {
            updateBookAuthors(book, resource.getBookAuthors());
        }

        return bookRepository.save(book);
    }

    public Book partialUpdateBook(final Long id, final EditBookRequest resource, final long desiredVersion) {
        final var book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[ERROR] Cannot update an object that does not yet exist"));

        Genre existingGenre = null;

        if (resource.getGenre() != null) {
            existingGenre = genreRepository.findById(resource.getGenre().getId()).orElseThrow(() -> new NotFoundException("[ERROR] Genre not found"));
        }

        book.applyPatch(desiredVersion, resource.getTitle(), existingGenre, resource.getDescription());

        if (resource.getBookAuthors() != null) {
            updateBookAuthors(book, resource.getBookAuthors());
        }
        return bookRepository.save(book);
    }

    private void validateCreateBookRequest(final CreateBookRequest request) {
        String bookTitle = request.getTitle();
        String trimmedTitle = bookTitle.trim();

        if(!bookTitle.equals(trimmedTitle)) {
            throw new IllegalArgumentException("[ERROR] Book Title cannot start or end with spaces.");
        }

        if (!BookUtil.isValidISBN(request.getIsbn())) {
            throw new IllegalArgumentException("[ERROR] ISBN-10 or ISBN-13 invalid ISBN.");
        }

        if (bookRepository.findBookByIsbn(request.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("[ERROR] Book with that ISBN is already registered.");
        }

        if (StringUtils.isBlank(request.getTitle()) ||
                StringUtils.isWhitespace(Character.toString(request.getTitle().charAt(0))) ||
                StringUtils.isWhitespace(Character.toString(request.getTitle().charAt(request.getTitle().length() - 1)))) {
            throw new IllegalArgumentException("[ERROR] Book title is mandatory and cannot start or end with spaces.");
        }

        if (StringUtils.isBlank(request.getGenre().getName()) || StringUtils.isBlank(request.getBookAuthors().toString())) {
            throw new IllegalArgumentException("[ERROR] Genre and author fields are mandatory.");
        }
    }

    @Transactional
    public void updateBookAuthors(final Book book, final List<BookAuthor> bookAuthorsList) {
        //delete das entradas antigas da tabela para depois podermos introduzir os novos bookAuthors
        bookAuthorRepository.deleteByBookId(book.getId());

        //save no book uma vez que vai ser preciso para criar associacoes entre book e author na tabela bookAuthors
        bookRepository.save(book);

        if (bookAuthorsList != null && !bookAuthorsList.isEmpty()) {
            List<BookAuthor> listBookAuthors = new ArrayList<>();
            for (BookAuthor bookAuthor : bookAuthorsList) {
                Author author = bookAuthor.getAuthor();
                if (author != null && author.getName() != null && author.getShortBio() != null) {
                    // Try to find the existing author by name
                    Author existingAuthor = authorRepository.findAuthorByName(author.getName())
                            .orElseThrow(() -> new IllegalArgumentException("[ERROR] Author with name '" + author.getName() + "' not found"));

                    // Add new BookAuthor association
                    listBookAuthors.add(new BookAuthor(book, existingAuthor));
                } else {
                    throw new IllegalArgumentException("[ERROR] Author information is incomplete for author: " + author.getName());
                }
            }
            // Save all new associations
            bookAuthorRepository.saveAll(listBookAuthors);
        }
    }

    public UploadFileResponse doUploadFile(final String id, final MultipartFile file) {

        if (isValidBookCover(file)) {
            BookCover cover = new BookCover();
            try {
                cover.setImage(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            cover.setContentType(file.getContentType());
            bookCoverRepository.save(cover);
            Book book = bookRepository.getById(Long.parseLong(id));
            book.setCover(cover);
            bookRepository.save(book);
        }

        final String fileName = fileStorageService.storeFile(id, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
                .toUriString();

        fileDownloadUri = fileDownloadUri.replace("/covers/", "/cover/");

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    public static Page<Book> toPage(List<Book> books, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        List<Book> sublist = books.subList(start, end);
        return new PageImpl<>(sublist, pageable, books.size());
    }

    public boolean bookExists(Long bookId) {
        return bookRepository.findById(bookId).isPresent();
    }
}
