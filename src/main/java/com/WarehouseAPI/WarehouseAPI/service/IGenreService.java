package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Genre;

import java.util.List;

public interface IGenreService {
    public String addGenre(Genre genre);
    public String updateGenre(String _id, Genre genre);
    public String deleteGenre(String _id);
    public Genre getGenre(String _id);
    public List<Genre> getAllGenre();

}
