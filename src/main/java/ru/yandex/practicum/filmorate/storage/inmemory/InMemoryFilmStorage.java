package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Optional<Film>> storage = new HashMap<>();
    private Long identifier = 0L;

    //Генерация ID номера
    public Long generationId() {
        identifier += 1;
        return identifier;
    }

    //Создание фильма
    public Film create(Film film) {
        boolean dd = storage.values().stream()
                .anyMatch(film1 -> film1.get().getName().equals(film.getName()));
        if (dd) {
            log.info("Невыполнено. Фильм с таким именем существует.");
            throw new ValidationException("Фильм с таким именем существует.");
        }
        film.setId(generationId());
        storage.put(film.getId(), Optional.of(film));
        log.info("Выполнено." + film.getDescription().length());
        return film;

    }

    //Обновление фильма
    public Film update(Film film) {
        if (storage.containsKey(film.getId())) {
            storage.put(film.getId(), Optional.of(film));
            log.info("Выполнено.");
            return film;
        } else {
            log.info("Такого фильма нет.");
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    //Получение фильма
    public Optional<Film> getFilm(Long id) {
        if (storage.containsKey(id)) {
            log.info("Фильм получен.");
            return storage.get(id);
        } else {
            log.info("Такого фильма нет.");
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    public List<Optional<Film>> getFilms() {
        List<Optional<Film>> films = new ArrayList<>(storage.values());
        log.info("Получен список фильмов.");
        return films;
    }

    @Override
    public void addLike(Long id, Long userId) {
        if (storage.containsKey(id) && storage.containsKey(userId)) {
            storage.get(id).get().getLikes().add(userId);
            log.info("Лайк фильму поставлен.");
        } else {
            log.info("Не найден фильм или Пользователь.");
            throw new NotFoundException("Не найден фильм или Пользователь.");
        }
    }

    @Override
    public List<Optional<Film>> getPopularFilm(Long count) {
        log.info("Получен список отсотритованных фильмов.");
        if (count <= 0) {
            count = 10L;
        }

        return storage.values().stream()
                .sorted((o1, o2) -> (o1.get().getLikes().size() - o2.get().getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        if (storage.containsKey(id) && storage.get(id).get().getLikes().contains(userId)) {
            storage.get(id).get().getLikes().remove(userId);
            log.info("Лайк у фильма  удален.");
        } else {
            log.info("Фильм или лайк не найден.");
            throw new NotFoundException("Фильм или лайк не найден.");
        }
    }

    @Override
    public boolean fidFilmByStorage(Long id) {
        if (storage.containsKey(id)) {
            return true;
        }
        return false;
    }
}
