package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.Genre;

import java.util.List;

public interface IGenreService {
    public String addGenre(Genre genre);
    public String updateGenre(String _id, Genre genre);
    public String deleteGenre(String _id);
    public Genre getGenre(String _id);

    List<Genre> findGenreByName(String value);

    public List<Genre> getAllGenre();

}
