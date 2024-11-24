package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService implements IGenreService{

    @Autowired
    GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository){
        this.genreRepository = genreRepository;
    }

    @Override
    public String addGenre(Genre genre) {
        genreRepository.save(genre);
        return "Add genre, ok";
    }

    @Override
    public String updateGenre(String _id, Genre updateGenre) {
        Optional<Genre> existingGenreOpt = genreRepository.findById(_id);
        if(existingGenreOpt.isPresent()){
            Genre existingGenre = existingGenreOpt.get();
            existingGenre.setGenreName(updateGenre.getGenreName());
            genreRepository.save(existingGenre);

            return "Update genre, ok";
        }
        return "Update genre, failed";
    }

    @Override
    public String deleteGenre(String idGenre) {
        genreRepository.deleteById(idGenre);
        return "Delete genre, ok";
    }

    @Override
    public Genre getGenre(String _id) {
        if(genreRepository.findById(_id).isEmpty())
        {
            return null;
        }
        return genreRepository.findById(_id).get();
    }

    @Override
    public List<Genre> getAllGenre() {
        return genreRepository.findAll();
    }
}
