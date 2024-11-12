package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.service.IGenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {
    IGenreService iGenreService;

    public GenreController(IGenreService iGenreService){
        this.iGenreService = iGenreService;
    }

    @GetMapping("/all")
    public List<Genre>  getAllGenreDetails(){
        return iGenreService.getAllGenre();
    }

    @GetMapping("/{_id}/get")
    public Genre getGenreDetails(@PathVariable("_id") String _id){
        return iGenreService.getGenre(_id);

    }

    @PostMapping("/add")
    public String addGenreDetails(@RequestBody Genre genre){
        iGenreService.addGenre(genre);
        return "Add genre, ok";
    }

    @PutMapping("/{_id}/update")
    public String updateGenreDetails(@PathVariable("_id") String _id, @RequestBody Genre genre){
        iGenreService.updateGenre(_id, genre);
        return "Update genre, ok";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteGenreDetails(@PathVariable("_id") String _id){
        iGenreService.deleteGenre(_id);
        return "Delete genre, ok";
    }

}
