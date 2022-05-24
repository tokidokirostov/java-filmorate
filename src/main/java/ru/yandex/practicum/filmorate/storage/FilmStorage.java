package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    public Map<Long, Film> getStorage();
    public Film create(Film film);
    public Film update(Film film);
    public void delete(Long id);
    public Film getFilm(Long id);
    public List<Film> getFilms();
}
