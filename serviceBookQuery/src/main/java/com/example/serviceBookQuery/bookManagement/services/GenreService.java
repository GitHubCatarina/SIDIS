package com.example.serviceBookQuery.bookManagement.services;

import com.example.serviceBookQuery.bookManagement.model.Genre;

import java.util.Optional;

public interface GenreService {
    Optional<Genre> getGenreById(final Long id);
    Iterable<Genre> getGenres();
}
