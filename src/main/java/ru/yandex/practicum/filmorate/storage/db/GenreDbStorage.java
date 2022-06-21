package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GANRES where GANRE_ID=?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("GANRE_ID"),
                    genreRows.getString("GANRE")
            );
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого жанра нет.");
        }
    }

    @Override
    public List<Optional<Genre>> findAllGenres() {
        String sql = "select GANRE_ID from GANRES";
        List<Optional<Genre>> ganres = jdbcTemplate.query(sql, (rs, rowNum) -> findGenreById(rs.getInt("GANRE_ID")));
        return ganres;
    }
}
