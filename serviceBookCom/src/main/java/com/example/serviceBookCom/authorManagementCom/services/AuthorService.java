package com.example.serviceBookCom.authorManagementCom.services;

import com.example.serviceBookCom.authorManagementCom.model.Author;
import com.example.serviceBookCom.authorManagementCom.model.AuthorPhoto;
import com.example.serviceBookCom.fileStorage.UploadFileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    int getTotalPages();
    Page<Author> getAuthors(Pageable pageable);
    Optional<Author> getAuthorsById(final Long id);
    AuthorPhoto getAuthorPhoto(final String authorId);
    List<Author> getAuthorsByName(final String name);
    Author createAuthor(final EditAuthorRequest resource, MultipartFile authorPhoto);
    Author updateAuthor(final Long id, final EditAuthorRequest resource, final long desiredVersion);
    Author partialUpdateAuthor(final Long id, final EditAuthorRequest resource, final long desiredVersion);
    UploadFileResponse doUploadFile(final String id, final MultipartFile file);
    void validateCreateAuthorRequest(final EditAuthorRequest request);
}