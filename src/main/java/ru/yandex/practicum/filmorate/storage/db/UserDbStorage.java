package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.*;

@Slf4j
@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    //Создание пользователя
    @Override
    public User create(User user) {
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)"
                , user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select USER_ID from USERS where EMAIL=?", user.getEmail());
        while (userRows.next()) {
            user.setId(userRows.getLong("USER_ID"));
        }
        return user;
    }

    //Обновление пользователя
    @Override
    public User update(User user) {
        jdbcTemplate.update("update USERS set EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? where USER_ID=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    //Получение пользователя
    @Override
    public Optional<User> getUser(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID=?", id);
        User user = new User();
        while (userRows.next()) {
            user.setId(userRows.getLong("USER_ID"));
            user.setEmail(userRows.getString("EMAIL"));
            user.setLogin(userRows.getString("LOGIN"));
            user.setName(userRows.getString("NAME"));
            user.setBirthday(userRows.getTimestamp("BIRTHDAY").toLocalDateTime().toLocalDate());
            Set<Long> friends = new TreeSet<>(getFriendsById(userRows.getLong("USER_ID")));
            user.setFriends(friends);
        }
        log.info("Найден пользователь: {} {}", user.getId(), user.getName());
        return Optional.of(user);
    }

    //Получение списка всех пользователей
    @Override
    public List<Optional<User>> getUsers() {
        String sql = "select USER_ID from USERS";
        List<Optional<User>> users = jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs.getLong("USER_ID")));
        return users;
    }

    //Поиск всех друзей пользователя
    @Override
    public List<Optional<User>> findAllUserFriends(Long userId) {
        String sql = "select USER_FRIEND_ID from FRIENDS where USER_ID_PK=" + userId;
        List<Optional<User>> users = jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs.getLong("USER_FRIEND_ID")));
        return users;
    }

    //список друзей, общих с другим пользователем
    @Override
    public List<Optional<User>> commonFriends(Long id, Long otherId) {
        return null;
    }

    //Добавление друга
    @Override
    public void addFriends(Long id, Long friendId) {
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID_PK, USER_FRIEND_ID) VALUES (?, ?)"
                , id, friendId);
        log.info("Пользователь добавлен в друзья.");
    }

    //Удаление друга
    @Override
    public void deleteFriend(Long id, Long fid) {
        jdbcTemplate.update("delete FROM FRIENDS WHERE USER_ID_PK=? AND USER_FRIEND_ID=?"
                , id, fid);
        log.info("Пользователь удален из друзей.");
    }

    @Override
    public boolean findUserByStorage(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select USER_ID from USERS where USER_ID=?", id);
        while (userRows.next()) {
            return true;
        }
        return false;
    }

    private List<Long> getFriendsById(Long id) {
        String sql = "select USER_FRIEND_ID from FRIENDS WHERE USER_ID_PK=" + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> (rs.getLong("USER_FRIEND_ID")));
    }
}
