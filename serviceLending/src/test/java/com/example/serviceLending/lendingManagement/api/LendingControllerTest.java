package com.example.serviceLending.lendingManagement.api;

import com.example.serviceLending.lendingManagement.model.Lending;
import com.example.serviceLending.lendingManagement.services.EditLendingRequest;
import com.example.serviceLending.lendingManagement.services.LendingServiceImpl;
import com.example.serviceLending.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LendingControllerTest {

    @InjectMocks
    private LendingController lendingController;

    @Mock
    private LendingServiceImpl lendingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLendingsPage() {
        // Configurar mock para a página de lendings
        when(lendingService.getLendings(any())).thenReturn(Page.empty());
        assertDoesNotThrow(() -> lendingController.getLendingsPage(0, 10, "Bearer token"));
        verify(lendingService, times(1)).getLendings(any());
    }

    @Test
    void getLending() {
        // Configurar mock para o lending
        when(lendingService.getLending(1L)).thenReturn(Optional.of(new Lending()));
        ResponseEntity<LendingView> response = lendingController.getLending(1L, "Bearer token");

        assertNotNull(response);
        verify(lendingService, times(1)).getLending(1L);
    }

    @Test
    void getLendingNotFound() {
        // Configurar mock para o caso em que o lending não é encontrado
        when(lendingService.getLending(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> lendingController.getLending(1L, "Bearer token"));
    }

    @Test
    void getOverdue() {
        // Configurar mock para os lendings em atraso
        when(lendingService.getOverdueLendings(any())).thenReturn(Page.empty());
        assertDoesNotThrow(() -> lendingController.getOverdue(0, 10, "Bearer token"));
        verify(lendingService, times(1)).getOverdueLendings(any());
    }

    @Test
    void getAverageLendingDuration() {
        // Configurar mock para a duração média de empréstimos
        when(lendingService.getAverageLendingDuration()).thenReturn(5.5);
        double result = lendingController.getAverageLendingDuration("Bearer token");

        assertEquals(5.5, result);
        verify(lendingService, times(1)).getAverageLendingDuration();
    }

    @Test
    void getAverageLendingDurationPerBook() {
        // Configurar mock para a duração média de empréstimos por livro
        when(lendingService.getAverageLendingDurationPerBook()).thenReturn(List.of());
        assertDoesNotThrow(() -> lendingController.getAverageLendingDurationPerBook("Bearer token"));
        verify(lendingService, times(1)).getAverageLendingDurationPerBook();
    }

    @Test
    void returnBook() {
        // Configurar mock para devolver livro
        Lending mockLending = new Lending();
        mockLending.setId(1L);
        when(lendingService.returnBook(any(), anyLong())).thenReturn(mockLending);

        ResponseEntity<LendingView> response = lendingController.returnBook(
                new EditLendingRequest(), "Bearer token", "W/\"123\"");

        assertNotNull(response);
        verify(lendingService, times(1)).returnBook(any(), anyLong());
    }

    @Test
    void getTopBooks() {
        // Configurar mock para os livros mais emprestados
        when(lendingService.getTopBooks()).thenReturn(List.of());
        assertDoesNotThrow(() -> lendingController.getTopBooks());
        verify(lendingService, times(1)).getTopBooks();
    }

    @Test
    void getTopReaders() {
        // Configurar mock para os leitores mais frequentes
        when(lendingService.getTopReaders()).thenReturn(List.of());
        assertDoesNotThrow(() -> lendingController.getTopReaders());
        verify(lendingService, times(1)).getTopReaders();
    }

    @Test
    void sincronizarComOutraInstancia() {
        // Simular sincronização com outra instância
        assertDoesNotThrow(() -> lendingController.sincronizarComOutraInstancia("Bearer token", new Lending()));
        // Não é necessário verificar interações, pois este método é chamado internamente
    }
}
