package com.example.serviceBookCom.bookManagement.services;

import com.example.serviceBookCom.bookManagement.model.Genre;

import java.util.Optional;

public interface GenreService {
    Optional<Genre> getGenreById(final Long id);
    Iterable<Genre> getGenres();
}
