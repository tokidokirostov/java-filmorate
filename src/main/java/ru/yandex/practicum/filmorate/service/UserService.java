package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    //Добавление в друзья
    public void addFriends(Long id, Long friendId) {
        if (userStorage.getStorage().containsKey(friendId)) {
            userStorage.getStorage().get(id).addFriends(friendId);
            userStorage.getStorage().get(friendId).addFriends(id);
            log.info("Пользователь добавлен в друзья.");
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }

    }

    //Найти всеx друзей пользователя
    public List<User> findAllUsers(Long id) {
        if (userStorage.getStorage().containsKey(id)) {
            List<User> list = new ArrayList<>();
            for (Long l : userStorage.getStorage().get(id).getFriends()) {
                list.add(userStorage.getStorage().get(l));
            }
            log.info("Получен список друзей пользователя");
            return list;
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    //Удалить пользователя из друзей
    public void deleteFriend(Long id, Long fid) {
        if (userStorage.getStorage().containsKey(id)) {
            userStorage.getStorage().get(id).getFriends().remove(fid);
            log.info("Пользователь удален из друзей.");
        } else {
            log.info("Друган не найден.");
            throw new NotFoundException("Друган не найден.");
        }
    }

    //Список друзей, общих с другим пользователем.
    public List<User> commonFriends(Long id, Long otherId) {
        Set<Long> userId = new TreeSet<>();
        Set<Long> userOtherId = new TreeSet<>();
        Set<Long> common = new TreeSet<>();
        List<User> commonUser = new ArrayList<>();
        userId = userStorage.getStorage().get(id).getFriends();
        userOtherId = userStorage.getStorage().get(otherId).getFriends();
        if (userId.isEmpty() && userOtherId.isEmpty()) {
            return commonUser;
        }
        if (userId.isEmpty() || userOtherId.isEmpty()) {
            log.info("Друганы не найдены.");
            throw new NotFoundException("Друганы не найдены.");
        } else {
            for (Long l : userId) {
                for (Long l2 : userOtherId) {
                    if (l == l2) {
                        common.add(l);
                    }
                }
            }
            for (Long l : common) {
                commonUser.add(userStorage.getStorage().get(l));
            }
        }
        log.info("Получен список друзей, общих с другим пользователем.");
        return commonUser;
    }
}
