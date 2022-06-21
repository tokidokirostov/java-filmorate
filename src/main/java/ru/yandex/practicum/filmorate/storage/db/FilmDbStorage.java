package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Optional<Film>> getStorage() {
        return null;
    }

    //Создание фильма
    @Override
    public Film create(Film film) {
        log.info("Создание фильма");
        jdbcTemplate.update("INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID_PK)" +
                        " VALUES (?, ?, ?, ?, ?)"
                , film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId());
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select FILM_ID from FILM where NAME=?", film.getName());
        if (filmRows.next()) {
            film.setId(filmRows.getInt("FILM_ID"));
        }
        if (!(film.getGenres() == null)) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("insert into film_ganres (film_id_pk, ganre_id_pk) " +
                        "VALUES (?, ?)", film.getId(), genre.getId());
                log.info("Добавление жанра {} в таблицу c film_id_PK {}", genre.getId(), film.getId());
            }
        }
        log.info(String.valueOf(film));
        return film;
    }

    //Обновление пользователя
    @Override
    public Film update(Film film) {
        log.info("Обновление фильма");
        Integer id = film.getId();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select FILM_ID from FILM where FILM_ID=?", id);
        Integer updateFilmId = 0;
        if (filmRows.next()) {
            updateFilmId = filmRows.getInt("FILM_ID");
        }
        //если айди в базе совпадает с айди в фильме то обновляем
        if (updateFilmId == id) {
            jdbcTemplate.update("update FILM set NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, RATING_ID_PK=?" +
                            " where FILM_ID=?", film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());
            if (film.getGenres() != null) {
                if (film.getGenres().isEmpty()) {
                    jdbcTemplate.update("delete from FILM_GANRES where FILM_ID_PK=?", film.getId());
                    film.setGenres(new ArrayList<>());
                    return film;
                }
                String sql = "select ganre_id_pk from FILM_GANRES where film_id_pk=" + film.getId();
                List<Integer> genreFilms = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("ganre_id_pk"));
                if (!genreFilms.isEmpty()) {
                    jdbcTemplate.update("delete from FILM_GANRES where FILM_ID_PK=?", film.getId());
                    log.info("Удаление старых жанров");
                }
                //идем по жанрам в списке
                SqlRowSet genreRows;
                for (Genre genre : film.getGenres()) {
                    genreRows = jdbcTemplate.queryForRowSet(
                            "select GANRE_ID_PK from FILM_GANRES where FILM_ID_PK=? and ganre_id_pk=?;"
                            , film.getId(), genre.getId());
                    if (genreRows.next()) {
                        jdbcTemplate.update("update film_ganres set ganre_id_pk=? where film_id_pk=?",
                                film.getId(), genre.getId());
                    } else {
                        jdbcTemplate.update("insert into film_ganres (film_id_pk, ganre_id_pk) " +
                                "VALUES (?, ?)", film.getId(), genre.getId());
                        log.info("Добавление жанра {} в таблицу c film_id_PK {}", genre.getId(), film.getId());
                    }
                }
            }
            log.info(String.valueOf(film));
            return getFilm(film.getId()).get();
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    //Получение фильма
    @Override
    public Optional<Film> getFilm(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where FILM_ID=?;", id);
        if (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("FILM_ID"));
            film.setName(filmRows.getString("NAME"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setReleaseDate(filmRows.getTimestamp("RELEASE_DATE").toLocalDateTime().toLocalDate());
            film.setDuration(filmRows.getInt("DURATION"));
            film.setMpa(getMpaById(filmRows.getInt("RATING_ID_PK")));
            film.setLikes(getLikesById(filmRows.getInt("FILM_ID")));
            if (!(getGenresById(filmRows.getInt("FILM_ID")).isEmpty())) {
                film.setGenres(getGenresById(filmRows.getInt("FILM_ID")));
            }
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого Фильма нет.");
        }
    }

    //Получение списка фильмов
    @Override
    public List<Optional<Film>> getFilms() {
        String sql = "select FILM_ID from FILM";
        List<Optional<Film>> films = jdbcTemplate.query(sql, (rs, rowNum) -> getFilm(rs.getInt("FILM_ID")));
        if (films.isEmpty()) {
            return films;
        } else {
            return films;
        }
    }

    //Добавление лайка
    @Override
    public void addLike(Integer id, Integer userId) {
        jdbcTemplate.update("INSERT INTO likes (FILM_ID_PK, USER_ID_PK) VALUES (?, ?)", id, userId);
        log.info("Фильму с №{} добвлен лайк от пользователя с №{}", id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    @Override
    public List<Optional<Film>> getPopularFilm(Integer count) {
        String sql = "select FILM_ID, count(USER_ID_PK)as users  from FILM as F\n" +
                "left outer join LIKES L on F.FILM_ID = L.FILM_ID_PK\n" +
                "group by FILM_ID order by users desc limit " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> getFilm(rs.getInt("FILM_ID")));
    }

    //Удаление лайка
    @Override
    public void deleteLike(Integer id, Integer userId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select USER_ID from USERS where USER_ID=?", userId);
        Integer userIdInBase = 0;
        if (filmRows.next()) {
            userIdInBase = filmRows.getInt("USER_ID");
        }
        if (userIdInBase == userId) {
            jdbcTemplate.update("delete from LIKES where FILM_ID_PK=? and USER_ID_PK=?", id, userId);
            log.info("У фильма №{} удалет лайк пользователя №{}", id, userId);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    //Получение лайков фильма
    private List<Integer> getLikesById(Integer id) {
        String sql = "select USER_ID_PK from LIKES WHERE FILM_ID_PK=" + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> (rs.getInt("USER_ID_PK")));
    }

    //Получение mpa
    private Mpa getMpaById(Integer id) {
        Mpa mpa = new Mpa();
        SqlRowSet ratingSet = jdbcTemplate.queryForRowSet("SELECT * FROM rating WHERE rating_id=?;", id);
        if (ratingSet.next()) {
            mpa.setId(ratingSet.getInt("RATING_ID"));
            mpa.setName(ratingSet.getString("RATING"));
            return mpa;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого Фильма нет.");
        }
    }

    //Получение жанров
    private List<Genre> getGenresById(Integer id) {
        String sql = "SELECT ganre_id_pk FROM film_ganres WHERE film_id_pk=" + id;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> (getGenreById(rs.getInt("ganre_id_pk")))));
    }

    //Получение жанра
    private Genre getGenreById(Integer id) {
        Genre genre = new Genre();
        SqlRowSet genreSet = jdbcTemplate.queryForRowSet("SELECT * FROM ganres WHERE ganre_id=?", id);
        if (genreSet.next()) {
            genre.setId(genreSet.getInt("ganre_id"));
            genre.setName(genreSet.getString("ganre"));
        }
        return genre;
    }
}