package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

        if (filmStorage.fidFilmByStorage(film.getId())) {
            return filmStorage.update(film);
        }else {
            log.info("Фильм с идентификатором {} не найден.", film.getId());
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    public Optional<Film> getFilm(Long id) {
        if (filmStorage.fidFilmByStorage(id)) {
            return filmStorage.getFilm(id);
        }else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого Фильма нет.");
        }
    }

    //Поставить лайк фильму
    public void addLike(Long id, Long userId) {
        if (filmStorage.fidFilmByStorage(id) && userStorage.findUserByStorage(userId)) {
            filmStorage.addLike(id, userId);
        } else {
            log.info("Фильм с идентификатором {} не найден или пользователь с идентификаторм {} не найден", id, userId);
            throw new NotFoundException("Такого Фильма или пользователя нет.");
        }
    }

    //Удаление лайка
    public void deleteLike(Long id, Long userId) {
        if (filmStorage.fidFilmByStorage(id) && userStorage.findUserByStorage(userId)) {
            filmStorage.deleteLike(id, userId);
        }else {
            log.info("Пользователь с идентификатором {} или фильм с идентификатором {} не найден.", userId, id);
            throw new NotFoundException("Такого пользователя нет.");
        }
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
