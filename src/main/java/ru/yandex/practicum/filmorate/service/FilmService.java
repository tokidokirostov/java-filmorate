package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    //Показать все фильмы
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    //Поставить лайк фильму
    public void addLike(Long id, Long userId) {
        if (filmStorage.getStorage().containsKey(id) && userStorage.getStorage().containsKey(userId)) {
            filmStorage.getFilm(id).getLikes().add(userId);
            log.info("Лайк фильму поставлен.");
        } else {
            log.info("Не найден фильм или Пользователь.");
            throw new NotFoundException("Не найден фильм или Пользователь.");
        }
    }

    //Удаление лайка
    public void deleteLike(Long id, Long userId) {
        if (filmStorage.getStorage().containsKey(id) && filmStorage.getStorage().get(id).getLikes().contains(userId)) {
            filmStorage.getStorage().get(id).getLikes().remove(userId);
            log.info("Лайк у фильма  удален.");
        } else {
            log.info("Фильм или лайк не найден.");
            throw new NotFoundException("Фильм или лайк не найден.");
        }
    }

    //Получить отсортированный список фильмов
    public List<Film> getPopularFilm(Integer count) {
        log.info("Получен список отсотритованных фильмов.");
        if (count <= 0) {
            count = 10;
        }
        return filmStorage.getStorage().values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesSize).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
