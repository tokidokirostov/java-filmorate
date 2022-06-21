package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    //public Map<Integer, Optional<Film>> getStorage();
    public Film create(Film film);
    public Film update(Film film);
    public Optional<Film> getFilm(Integer id);
    public List<Optional<Film>> getFilms();
    public void addLike(Integer id, Integer userId);
    public List<Optional<Film>> getPopularFilm(Integer count);
    public void deleteLike(Integer id, Integer userId);
}
