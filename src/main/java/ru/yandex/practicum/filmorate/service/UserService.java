package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
        return userStorage.getUsers();
    }

    public User create(User user) {

        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        //userDao.create(user);
        //getStorage().put(user.getId(), user);
        log.info("Создан пользователь.");
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        //userDao.create(user);
        //getStorage().put(user.getId(), user);
        log.info("Создан пользователь.");
        //return userStorage.create(user);

        return userStorage.update(user);
    }

    public Optional<User> getUser(Long id) {
        return userStorage.getUser(id);
    }

    //Добавление в друзья
    public void addFriends(Long id, Long friendId) {
userStorage.addFriends(id, friendId);
    }

    public List<Optional<User>> findAllUserFriends(Long userId){

        return userStorage.findAllUserFriends(userId);
    }

    //Удалить пользователя из друзей
    public void deleteFriend(Long id, Long fid){
        userStorage.deleteFriend(id, fid);
    }

    public List<Optional<User>> commonFriends(Long id, Long otherId) {
        return userStorage.commonFriends(id, otherId);
    }

}