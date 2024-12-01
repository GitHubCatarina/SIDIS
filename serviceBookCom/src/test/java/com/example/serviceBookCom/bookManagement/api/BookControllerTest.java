package com.example.serviceBookCom.bookManagement.api;

import com.example.serviceBookCom.bookManagement.model.Book;
import com.example.serviceBookCom.bookManagement.model.BookCover;
import com.example.serviceBookCom.bookManagement.services.BookServiceImpl;
import com.example.serviceBookCom.bookManagement.services.CreateBookRequest;
import com.example.serviceBookCom.bookManagement.services.EditBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile() {
        assertDoesNotThrow(() -> bookController.uploadFile("123", null, "Bearer token"));
        verify(bookService, times(1)).doUploadFile(eq("123"), any());
    }

    @Test
    void createBook() {
        assertDoesNotThrow(() -> bookController.createBook(new CreateBookRequest(), null, "Bearer token"));
        verify(bookService, times(1)).createBook(any(), any());
    }

    @Test
    void updateBook() {
        assertDoesNotThrow(() -> bookController.updateBook(null, 1L, new EditBookRequest(), "Bearer token"));
        verify(bookService, times(1)).updateBook(eq(1L), any(), anyLong());
    }

    @Test
    void partialUpdateBook() {
        assertDoesNotThrow(() -> bookController.partialUpdateBook(null, 1L, new EditBookRequest(), "Bearer token"));
        verify(bookService, times(1)).partialUpdateBook(eq(1L), any(), anyLong());
    }
}
