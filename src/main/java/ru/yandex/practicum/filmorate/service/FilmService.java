package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Optional<Film>> getFilms() {

        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Optional<Film> getFilm(Long id) {

        return filmStorage.getFilm(id);
    }

    //Поставить лайк фильму
    public void addLike(Long id, Long userId) {
        filmStorage.addLike(id, userId);
    }

    //Удаление лайка
    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id, userId);
    }

    //Получить отсортированный список фильмов
    public List<Optional<Film>> getPopularFilm(Long count) {
        log.info("Получен список отсотритованных фильмов.");
        if (count <= 0) {
            count = 10L;
        }
        return filmStorage.getPopularFilm(count);
    }
}
