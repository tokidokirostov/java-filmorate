package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> storage = new HashMap<>();
    private Long identifier = 0L;

    public Map<Long, Film> getStorage() {
        return storage;
    }

    //Генерация ID номера
    public Long generationId() {
        identifier += 1;
        return identifier;
    }

    //Создание фильма
    public Film create(Film film) {
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

    //Обновление фильма
    public Film update(Film film) {
        if (storage.containsKey(film.getId())) {
            storage.put(film.getId(), film);
            log.info("Выполнено.");
            return film;
        } else {
            log.info("Такого фильма нет.");
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    //Удаление фильма
    public void delete(Long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            log.info("Фильм удален.");
        } else {
            log.info("Такого фильма нет.");
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    //Получение фильма
    public Film getFilm(Long id) {
        if (storage.containsKey(id)) {
            log.info("Фильм получен.");
            return storage.get(id);
        } else {
            log.info("Такого фильма нет.");
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>(storage.values());
        log.info("Получен список фильмов.");
        return films;
    }
}
