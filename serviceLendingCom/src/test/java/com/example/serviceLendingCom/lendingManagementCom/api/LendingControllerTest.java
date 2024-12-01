package com.example.serviceLendingCom.lendingManagementCom.api;

import com.example.serviceLendingCom.exceptions.NotFoundException;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import com.example.serviceLendingCom.lendingManagementCom.services.EditLendingRequest;
import com.example.serviceLendingCom.lendingManagementCom.services.LendingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

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

    /*
    @Test
    void getLendingsPage() {
        // Configurar mock para a página de lendings
        when(lendingService.getLendings(any())).thenReturn(Page.empty());
        assertDoesNotThrow(() -> lendingController.getLendingsPage(0, 10, "Bearer token"));
        verify(lendingService, times(1)).getLendings(any());
    }

     */




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

}
