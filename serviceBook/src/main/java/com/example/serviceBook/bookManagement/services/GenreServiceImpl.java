package com.example.serviceBook.bookManagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.serviceBook.bookManagement.model.Genre;
import com.example.serviceBook.bookManagement.repositories.GenreRepository;
import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService{
    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    public Optional<Genre> getGenreById(final Long id) {
        return genreRepository.findGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

}
