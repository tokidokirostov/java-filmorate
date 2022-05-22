package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> storage = new HashMap<>();
    private Long identifier = 0L;

    public Map<Long, Film> hello2() {
        Film film1 = new Film(1L, "Cat", "Мой любимый котик!!", LocalDate.of(1991, 10, 1), 90);
        Film film2 = new Film(2L, "Cat2", "Мой любимый котик!!2", LocalDate.of(1992, 10, 1), 90);
        Film film3 = new Film(3L, "Cat3", "Мой любимый котик!!3", LocalDate.of(1993, 10, 1), 90);

        storage.put(film1.getId(), film1);
        storage.put(film2.getId(), film2);
        storage.put(film3.getId(), film3);

        return storage;
    }

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
}
