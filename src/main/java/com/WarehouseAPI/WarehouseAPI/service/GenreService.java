package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.repository.GenreRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService implements IGenreService {

    @Autowired
    GenreRepository genreRepository;
    private final MongoTemplate mongoTemplate;

    public GenreService(GenreRepository genreRepository, MongoTemplate mongoTemplate){
        this.genreRepository = genreRepository;
        this.mongoTemplate = mongoTemplate;
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
    public List<Genre> findGenreByName(String value){
        try {
            Criteria criteria = Criteria.where("genreName").regex(value, "i");
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria));

            AggregationResults<Genre> results = mongoTemplate.aggregate(
                    aggregation, "genre", Genre.class);
            return results.getMappedResults();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching genre", e);
        }

    }

    @Override
    public List<Genre> getAllGenre() {
        return genreRepository.findAll();
    }
}
