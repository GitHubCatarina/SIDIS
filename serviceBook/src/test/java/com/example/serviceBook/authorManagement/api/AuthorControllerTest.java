package com.example.serviceBook.authorManagement.api;

import com.example.serviceBook.authorManagement.services.AuthorServiceImpl;
import com.example.serviceBook.authorManagement.services.EditAuthorRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    @InjectMocks
    private AuthorController authorController;

    @Mock
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTopAuthors() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.getTopAuthors("Bearer token"));
        verify(authorService, times(1)).getTopAuthors();
    }

    @Test
    void getAuthors() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.getAuthors(0, 100, "Bearer token"));
        verify(authorService, times(1)).getAuthors(any());
    }

    @Test
    void getAuthor() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.getAuthor(1L, "Bearer token"));
        verify(authorService, times(1)).getAuthorsById(1L);
    }

    @Test
    void testGetAuthors() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.getAuthors("Author Name", "Bearer token"));
        verify(authorService, times(1)).getAuthorsByName("Author Name");
    }

    @Test
    void getBookCover() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.getBookCover("1", "Bearer token"));
        verify(authorService, times(1)).getAuthorPhoto("1");
    }

    @Test
    void createAuthor() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.createAuthor(new EditAuthorRequest(), null, "Bearer token"));
        verify(authorService, times(1)).createAuthor(any(), any());
    }

    @Test
    void uploadFile() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.uploadFile("1", null, "Bearer token"));
        verify(authorService, times(1)).doUploadFile(eq("1"), any());
    }

    @Test
    void updateAuthor() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.updateAuthor(null, 1L, new EditAuthorRequest(), "Bearer token"));
        verify(authorService, times(1)).updateAuthor(eq(1L), any(), anyLong());
    }

    @Test
    void partialUpdateAuthor() {
        // Mock e verificação simples
        assertDoesNotThrow(() -> authorController.partialUpdateAuthor(null, 1L, new EditAuthorRequest(), "Bearer token"));
        verify(authorService, times(1)).partialUpdateAuthor(eq(1L), any(), anyLong());
    }
}
