package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    private final FilmService filmService;

    private FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //Получение всех фильмов
    @GetMapping
    public Collection<Optional<Film>> findAll() {
        log.info("Получен запрос GET все фильмы");
        return filmService.getFilms();
    }

    //Добавление фильма
    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films. Создание фильма.");
        log.info(film.getMpa().toString());
        if (!(film.getGenres() == null)) {
            log.info(film.getGenres().toString());
        } else {
            log.info("Жанров нет");
        }
        return filmService.create(film);
    }

    //Обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /film. Обновление фильма.");
        return filmService.update(film);
    }

    //Получение фильма
    @GetMapping("/{id}")
    public Optional<Film> getFilm(@PathVariable Long id) {
        log.info("Получен запрос GET /{} ", id);
        return filmService.getFilm(id);
    }

    //Лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос PUT /{}/like/{}. Лайк фильму.", id, userId);
        filmService.addLike(id, userId);
    }

    //Удаление лайка
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос DELETE /{}/like/{}. Удаление лайка фильма.", id, userId);
        filmService.deleteLike(id, userId);
    }

    //Возвращает список фильмов по количеству лайков
    @GetMapping("/popular")
    public List<Optional<Film>> getPopularFilm(
            @RequestParam(value = "count", required = false, defaultValue = "10") Long count) {
        log.info("Получен запрос GET /popular?count={} ", count);
        return filmService.getPopularFilm(count);
    }

}
