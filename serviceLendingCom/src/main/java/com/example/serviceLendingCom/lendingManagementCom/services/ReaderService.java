package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.fileStorage.UploadFileResponse;
import com.example.serviceLendingCom.lendingManagementCom.model.Reader;
import com.example.serviceLendingCom.lendingManagementCom.model.ReaderPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ReaderService {
    Page<Reader> getReaders(Pageable pageable);
    Iterable<Reader> getAllReaders();
    //Iterable<Reader> getTopReaders(int topN);
    //Iterable<Reader> getTopReadersperGenre(int topN, Genre genre, LocalDate startDate, LocalDate endDate);
    Optional<Reader> getReaderByIdWithQuote(Long id);
    //int getMonthlyLending(Long readerId, LocalDate startDate, LocalDate endDate);
    //Page<Book> getSuggestedBooks(Long readerId, Pageable pageable);
    Page<Reader> getReadersByPhoneNumberAndEmail(final String phoneNumber, final String email, Pageable pageable);
    Page<Reader> getReadersByPhoneNumber(final String phoneNumber, Pageable pageable);
    Page<Reader> getReadersByEmail(final String email, Pageable pageable);
    ReaderPhoto getReaderPhoto(final String readerId);
    Reader createReader(final EditReaderRequest resource, MultipartFile photo);
    Reader updateReader(final Long id, final EditReaderRequest resource, final long desiredVersion);
    Reader partialUpdateReader(final Long id, final EditReaderRequest resource, final long desiredVersion);
    UploadFileResponse doUploadFile(final String id, final MultipartFile file);
   // public List<ReaderView> getTopReaders();

}
