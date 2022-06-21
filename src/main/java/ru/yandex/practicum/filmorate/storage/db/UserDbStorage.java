package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Optional<User>> getStorage() {
        return null;
    }

    //Создание пользователя
    @Override
    public User create(User user) {
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)"
                , user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select USER_ID from USERS where EMAIL=?", user.getEmail());
        if (userRows.next()) {
            user.setId(userRows.getInt("USER_ID"));
        }
        return user;

    }

    //Обновление пользователя
    @Override
    public User update(User user) {
        Integer id = user.getId();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select USER_ID from USERS where USER_ID=?", id);
        Integer updateUserId = 0;
        if (userRows.next()) {
            updateUserId = userRows.getInt("USER_ID");
        }
        if (updateUserId == id) {
            jdbcTemplate.update("update USERS set EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? where USER_ID=?",
                    user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    //Получение пользователя
    @Override
    public Optional<User> getUser(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID=?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("USER_ID"),
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME"),
                    userRows.getTimestamp("BIRTHDAY").toLocalDateTime().toLocalDate()
            );
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    //Получение списка всех пользователей
    @Override
    public List<Optional<User>> getUsers() {
        String sql = "select USER_ID from USERS";
        List<Optional<User>> users = jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs.getInt("USER_ID")));
        if (users.isEmpty()) {
            return null;
        } else {
            return users;
        }
    }

    //Поиск всех друзей пользователя
    @Override
    public List<Optional<User>> findAllUserFriends(Integer userId) {
        String sql = "select USER_FRIEND_ID from FRIENDS where USER_ID_PK=" + userId;
        List<Optional<User>> users = jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs.getInt("USER_FRIEND_ID")));
        return users;
    }

    //список друзей, общих с другим пользователем
    @Override
    public List<Optional<User>> commonFriends(Integer id, Integer otherId) {
        String userFriends = "select USER_FRIEND_ID from FRIENDS WHERE USER_ID_PK=" + id;
        List<Integer> userFriendsAll = jdbcTemplate.query(userFriends, (rs, rowNum) ->
                rs.getInt("USER_FRIEND_ID"));
        String friendFriends = "select USER_FRIEND_ID from FRIENDS WHERE USER_ID_PK=" + otherId;
        List<Integer> friendFriendsAll = jdbcTemplate.query(friendFriends, (rs, rowNum) ->
                rs.getInt("USER_FRIEND_ID"));
        userFriendsAll.retainAll(friendFriendsAll);
        List<Optional<User>> commonFriends = new ArrayList<>();
        for (Integer ids : userFriendsAll) {
            commonFriends.add(getUser(ids));
        }
        return commonFriends;
    }

    //Добавление друга
    @Override
    public void addFriends(Integer id, Integer friendId) {
        Integer userIdInBase = 0;
        Integer friendsIdInBase = 0;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select USER_ID from USERS where USER_ID=?", id);
        if (userRows.next()) {
            userIdInBase = (userRows.getInt("USER_ID"));
        }
        SqlRowSet userFriendsRows = jdbcTemplate.queryForRowSet(
                "select USER_ID from USERS where USER_ID=?", friendId);
        if (userFriendsRows.next()) {
            friendsIdInBase = (userFriendsRows.getInt("USER_ID"));
        }

        if (userIdInBase > 0 && friendsIdInBase > 0) {
            jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID_PK, USER_FRIEND_ID) VALUES (?, ?)"
                    , userIdInBase, friendsIdInBase);
            log.info("Пользователь добавлен в друзья.");
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    //Удаление друга
    @Override
    public void deleteFriend(Integer id, Integer fid) {
        Integer friendsIdDelete = 0;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT FRIENDS_ID FROM FRIENDS WHERE USER_ID_PK=? AND USER_FRIEND_ID=?", id, fid);
        if (userRows.next()) {
            friendsIdDelete = (userRows.getInt("FRIENDS_ID"));
        }
        if (friendsIdDelete > 0) {
            jdbcTemplate.update("delete FROM FRIENDS WHERE FRIENDS_ID=?"
                    , friendsIdDelete);
            log.info("Пользователь удален из друзей.");
        } else {
            log.info("Друг - {} - не найден.", fid);
            throw new NotFoundException("Пользователь не найден.");
        }
    }
}
