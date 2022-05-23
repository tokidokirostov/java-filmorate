package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.io.File;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    private final FilmService filmService;

    private FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmService = filmService;
    }

    //Получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос GET все фильмы");
        return filmService.getFilmStorage().getStorage().values();
    }

    //Добавление фильма
    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films. Создание фильма.");
        return filmService.getFilmStorage().create(film);
    }

    //Обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /film. Обновление фильма.");
        return filmService.getFilmStorage().update(film);
    }

    //Удаление фильма
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос DELETE /{}. удаление фильма.", id);
        filmService.getFilmStorage().delete(id);
    }

    //Получение фильма
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("Получен запрос GET /{} ", id);
        return filmService.getFilmStorage().getFilm(id);
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
    public List<Film> getPopularFilm(
            @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        log.info("Получен запрос GET /popular?count={} ", count);
        return filmService.getPopularFilm(count);
    }

}
