package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findMpaById(Long id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from RATING where RATING_ID=?", id);
        if(genreRows.next()){
            Mpa mpa = new Mpa(
                    genreRows.getLong("RATING_ID"),
                    genreRows.getString("RATING")
            );
            log.info("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого рейтинга нет.");
        }
    }

    @Override
    public List<Optional<Mpa>> findAllMpa() {
        String sql = "select RATING_ID from RATING";
        List<Optional<Mpa>> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> findMpaById(rs.getLong("RATING_ID")));
        return mpa;
    }
    }