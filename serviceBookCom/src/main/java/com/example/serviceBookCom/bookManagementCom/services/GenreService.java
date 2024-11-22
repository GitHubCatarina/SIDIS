package com.example.serviceBookCom.bookManagementCom.services;

import com.example.serviceBookCom.bookManagementCom.model.Genre;

import java.util.Optional;

public interface GenreService {
    Optional<Genre> getGenreById(final Long id);
    Iterable<Genre> getGenres();
}
