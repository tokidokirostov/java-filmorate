package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films. Создание фильма.");
        boolean dd = getStorage().values().stream()
                .anyMatch(film1 -> film1.getName().equals(film.getName()));

        if (dd) {
            log.info("Невыполнено. Фильм с таким именем существует.");
            throw new ValidationException("Фильм с таким именем существует.");
        }
        film.setId(generationId());
        getStorage().put(film.getId(), film);
        log.info("Выполнено." + film.getDescription().length());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /film. Обновление фильма.");
        log.info("Выполнено.");
        return film;
    }
}
