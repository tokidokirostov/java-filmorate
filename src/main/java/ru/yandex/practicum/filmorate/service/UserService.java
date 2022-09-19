package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<Optional<User>> getUsers() {
        if (userStorage.getUsers().isEmpty()) {
            return null;
        }
        return userStorage.getUsers();
    }

    public User create(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        log.info("Создан пользователь.");
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        log.info("Пользователь обновлен.");
        if (userStorage.findUserByStorage(user.getId())) {
            log.info("User id - {}", user.getId());
            log.info("User id in base - {}", getUser(user.getId()).get().getId());
            return userStorage.update(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", user.getId());
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public Optional<User> getUser(Long id) {
        if (userStorage.findUserByStorage(id)) {
            return userStorage.getUser(id);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    //Добавление в друзья
    public void addFriends(Long id, Long friendId) {
        if (userStorage.findUserByStorage(id) && userStorage.findUserByStorage(friendId)) {
            userStorage.addFriends(id, friendId);
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public List<Optional<User>> findAllUserFriends(Long userId) {
        return userStorage.findAllUserFriends(userId);
    }

    //Удалить пользователя из друзей
    public void deleteFriend(Long id, Long fid) {
        if (userStorage.findUserByStorage(id) && userStorage.findUserByStorage(fid)) {
            userStorage.deleteFriend(id, fid);
        } else {
            log.info("Друг - {} - не найден.", fid);
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public List<Optional<User>> commonFriends(Long id, Long otherId) {
        List<Optional<User>> userId = new ArrayList<>(findAllUserFriends(id));
        List<Optional<User>> userOtherId = new ArrayList<>(findAllUserFriends(otherId));
        userId.retainAll(userOtherId);
        return userId;
    }

}