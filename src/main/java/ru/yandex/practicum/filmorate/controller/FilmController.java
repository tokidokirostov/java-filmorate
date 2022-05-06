package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int identifier = 0;
    // создаём логер
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос POST /films");
        boolean dd = films.values().stream()
                .anyMatch(film1 -> film1.getName().equals(film.getName()));

        if (film.getName().equals("")) {
            log.info("Создание нового фильма. Невыполнено. Отсутствует название фильма.");
            throw new ValidationException("Отсутствует название фильма.");

        }
        if (film.getDescription().isEmpty() || film.getDescription().equals("")) {
            log.info("Обновление фильма фильма. Невыполнено. Описание 0 символов.");
            throw new ValidationException("Описание 0 символов.");
        }
        if (film.getDescription().length() > 200) {
            log.info("Создание нового фильма. Невыполнено. Описание больше 200 символов. {}"
                    , film.getDescription().length());
            throw new ValidationException("Описание больше 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Создание нового фильма. Невыполнено. Фильм старше 28 декабря 1895 года.");
            throw new ValidationException("Фильм старше 28 декабря 1895 года.");

        }
        if (film.getDuration().toMinutes() < 1) {
            log.info("Создание нового фильма. Невыполнено. Продолжительность фильма отрицательная или равна нулю");
            throw new ValidationException("Продолжительность фильма отрицательная или равна нулю.");

        }
        if (dd) {
            log.info("Создание нового фильма. Невыполнено. Фильм с таким именем существует.");
            throw new ValidationException("Фильм с таким именем существует.");
        }
        identifier++;
        film.setId(identifier);
        log.info("Создание нового фильма. Выполнено." + film.getDescription().length());
        films.put(identifier, film);

        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос PUT /film.");
        if (film.getName().equals("")) {
            log.info("Обновление фильма фильма. Невыполнено. Отсутствует название фильма.");
            throw new ValidationException("Отсутствует название фильма.");
        }
        if (film.getDescription().isEmpty() || film.getDescription().equals("")) {
            log.info("Обновление фильма фильма. Невыполнено. Описание 0 символов.");
            throw new ValidationException("Описание 0 символов.");
        }
        if (film.getDescription().length() > 200) {
            log.info("Обновление фильма фильма. Невыполнено. Описание больше 200 символов. {}"
                    , film.getDescription().length());
            throw new ValidationException("Описание больше 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Обновление фильма фильма. Невыполнено. Фильм старше 28 декабря 1895 года.");
            throw new ValidationException("Фильм старше 28 декабря 1895 года.");
        }
        if (film.getDuration().toMinutes() < 1) {
            log.info("Обновление фильма фильма. Невыполнено. Продолжительность фильма отрицательная или равна нулю.");
            throw new ValidationException("Продолжительность фильма отрицательная или равна нулю.");
        }
        log.info("Обновление фильма фильма. Выполнено.");
        films.put(film.getId(), film);

        return film;
    }
}
