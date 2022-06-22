package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    public Film create(Film film);
    public Film update(Film film);
    public Optional<Film> getFilm(Long id);
    public List<Optional<Film>> getFilms();
    public void addLike(Long id, Long userId);
    public List<Optional<Film>> getPopularFilm(Long count);
    public void deleteLike(Long id, Long userId);
    public boolean fidFilmByStorage(Long id);
}
