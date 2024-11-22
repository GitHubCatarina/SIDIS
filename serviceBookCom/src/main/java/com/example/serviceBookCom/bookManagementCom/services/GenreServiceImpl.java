package com.example.serviceBookCom.bookManagementCom.services;

import com.example.serviceBookCom.bookManagementCom.model.Genre;
import com.example.serviceBookCom.bookManagementCom.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
