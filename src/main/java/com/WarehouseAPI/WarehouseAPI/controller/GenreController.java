package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IGenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {

    private final IGenreService genreService;
    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenreDetails() {
        return genreService.getAllGenre();
    }

    @GetMapping("/{id}")
    public Genre getGenreDetails(@PathVariable String id) {
        return genreService.getGenre(id);
    }

    @GetMapping("/search")
    public List<Genre> getSearchedGenresDetails(@RequestParam("value") String value){
        return genreService.findGenreByName(value);
    }

    @PostMapping
    public String addGenreDetails(@RequestBody Genre genre) {
        genreService.addGenre(genre);
        return "Genre added successfully";
    }

    @PutMapping("/{id}")
    public String updateGenreDetails(@PathVariable String id, @RequestBody Genre genre) {
        genreService.updateGenre(id, genre);
        return "Genre updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteGenreDetails(@PathVariable String id) {
        genreService.deleteGenre(id);
        return "Genre deleted successfully";
    }
}