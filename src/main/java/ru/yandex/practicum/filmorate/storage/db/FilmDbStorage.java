package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
            film.setId(filmRows.getLong("FILM_ID"));
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
            List<Long> genreFilms = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("ganre_id_pk"));
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
    }

    //Получение фильма
    @Override
    public Optional<Film> getFilm(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where FILM_ID=?;", id);
        Film film = new Film();
        while (filmRows.next()) {
            film.setId(filmRows.getLong("FILM_ID"));
            film.setName(filmRows.getString("NAME"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setReleaseDate(filmRows.getTimestamp("RELEASE_DATE").toLocalDateTime().toLocalDate());
            film.setDuration(filmRows.getLong("DURATION"));
            film.setMpa(getMpaById(filmRows.getLong("RATING_ID_PK")));
            film.setLikes(getLikesById(filmRows.getLong("FILM_ID")));
            if (!(getGenresById(filmRows.getLong("FILM_ID")).isEmpty())) {
                film.setGenres(getGenresById(filmRows.getLong("FILM_ID")));
            }
        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return Optional.of(film);
    }

    //Получение списка фильмов
    @Override
    public List<Optional<Film>> getFilms() {
        String sql = "select FILM_ID from FILM";
        List<Optional<Film>> films = jdbcTemplate.query(sql, (rs, rowNum) -> getFilm(rs.getLong("FILM_ID")));
        return films;
    }

    //Добавление лайка
    @Override
    public void addLike(Long id, Long userId) {
        jdbcTemplate.update("INSERT INTO likes (FILM_ID_PK, USER_ID_PK) VALUES (?, ?)", id, userId);
        log.info("Фильму с №{} добвлен лайк от пользователя с №{}", id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков
    @Override
    public List<Optional<Film>> getPopularFilm(Long count) {
        String sql = "select FILM_ID, count(USER_ID_PK)as users  from FILM as F\n" +
                "left outer join LIKES L on F.FILM_ID = L.FILM_ID_PK\n" +
                "group by FILM_ID order by users desc limit " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> getFilm(rs.getLong("FILM_ID")));
    }

    //Удаление лайка
    @Override
    public void deleteLike(Long id, Long userId) {
        jdbcTemplate.update("delete from LIKES where FILM_ID_PK=? and USER_ID_PK=?", id, userId);
        log.info("У фильма №{} удалет лайк пользователя №{}", id, userId);
    }

    //Получение лайков фильма
    private List<Long> getLikesById(Long id) {
        String sql = "select USER_ID_PK from LIKES WHERE FILM_ID_PK=" + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> (rs.getLong("USER_ID_PK")));
    }

    //Получение mpa
    private Mpa getMpaById(Long id) {
        Mpa mpa = new Mpa();
        SqlRowSet ratingSet = jdbcTemplate.queryForRowSet("SELECT * FROM rating WHERE rating_id=?;", id);
        while (ratingSet.next()) {
            mpa.setId(ratingSet.getLong("RATING_ID"));
            mpa.setName(ratingSet.getString("RATING"));
        }
        return mpa;
    }

    public boolean fidFilmByStorage(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select FILM_ID from FILM where FILM_ID=?", id);
        while (filmRows.next()) {
            return true;
        }
        return false;
    }

    //Получение жанров
    private List<Genre> getGenresById(Long id) {
        String sql = "SELECT ganre_id_pk FROM film_ganres WHERE film_id_pk=" + id;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> (getGenreById(rs.getLong("ganre_id_pk")))));
    }

    //Получение жанра
    private Genre getGenreById(Long id) {
        Genre genre = new Genre();
        SqlRowSet genreSet = jdbcTemplate.queryForRowSet("SELECT * FROM ganres WHERE ganre_id=?", id);
        while (genreSet.next()) {
            genre.setId(genreSet.getLong("ganre_id"));
            genre.setName(genreSet.getString("ganre"));
        }
        return genre;
    }
}
