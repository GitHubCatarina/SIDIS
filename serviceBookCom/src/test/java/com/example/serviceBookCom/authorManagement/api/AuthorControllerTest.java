package com.example.serviceBookCom.authorManagement.api;

import com.example.serviceBookCom.authorManagement.services.AuthorServiceImpl;
import com.example.serviceBookCom.authorManagement.services.EditAuthorRequest;
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
