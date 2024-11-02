package com.example.serviceBook.bookManagement.services;

import com.example.serviceBook.bookManagement.model.Genre;

import java.util.Optional;

public interface GenreService {
    Optional<Genre> getGenreById(final Long id);
    Iterable<Genre> getGenres();
}
