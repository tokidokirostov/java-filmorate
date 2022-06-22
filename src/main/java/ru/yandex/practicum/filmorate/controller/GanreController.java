package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GanreController {
GenreService genreService;

    public GanreController(GenreService genreService) {
        this.genreService = genreService;
    }

@GetMapping
    public List<Optional<Genre>> findAll() {
        log.info("Получен запрос GET на список жанров");
        return genreService.findAllGenres();
    }

    @GetMapping("/{id}")
    public Optional<Genre> findById(@PathVariable Long id) {
        log.info("Получен запрос GET /genres/{}", id);
        return genreService.findById(id);
    }
}
