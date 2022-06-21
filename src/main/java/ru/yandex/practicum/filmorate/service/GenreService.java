package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
//GenreStorage genreStorage;
    //GenreDao genreDao;
    GenreStorage genreStorage;

    /*public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }*/

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Optional<Genre> findById(Integer id) {
        return genreStorage.findGenreById(id);
}
    public List<Optional<Genre>> findAllGenres(){
        return genreStorage.findAllGenres();
    }

}
