package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, Optional<User>> storage = new HashMap<>();
    private Integer identifier = 0;

    //Генерация ID номера
    public Integer generationId() {
        identifier += 1;
        return identifier;
    }

    //Создание пользователя
    public User create(User user) {
        user.setId(generationId());
        storage.put(user.getId(), Optional.of(user));
        log.info("Создан пользователь.");
        return user;
    }

    //Обновление пользователя
    public User update(User user) {
        if (storage.containsKey(user.getId())) {
            storage.put(user.getId(), Optional.of(user));
            log.info("Пользователь обновлен.");
            return user;
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    //показать пользователя
    public Optional<User> getUser(Integer id) {
        if (storage.containsKey(id)) {
            log.info("Пользователь получен.");
            return storage.get(id);
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public List<Optional<User>> getUsers() {
        List<Optional<User>> users = new ArrayList<>(storage.values());
        log.info("Получен список пользователей.");
        return users;
    }

    @Override
    public List<Optional<User>> findAllUserFriends(Integer userId) {
        if (storage.containsKey(userId)) {
            List<Optional<User>> list = new ArrayList<>();
            for (Integer l : storage.get(userId).get().getFriends()) {
                list.add(storage.get(l));
            }
            log.info("Получен список друзей пользователя");
            return list;
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    //Список друзей, общих с другим пользователем.
    @Override
    public List<Optional<User>> commonFriends(Integer id, Integer otherId) {
        Set<Integer> userId = new TreeSet<>();
        Set<Integer> userOtherId = new TreeSet<>();
        Set<Integer> common = new TreeSet<>();
        List<Optional<User>> commonUser = new ArrayList<>();
        userId = storage.get(id).get().getFriends();
        userOtherId = storage.get(otherId).get().getFriends();
        if (userId.isEmpty() && userOtherId.isEmpty()) {
            return commonUser;
        }
        if (userId.isEmpty() || userOtherId.isEmpty()) {
            log.info("Друганы не найдены.");
            throw new NotFoundException("Друганы не найдены.");
        } else {
            for (Integer l : userId) {
                for (Integer l2 : userOtherId) {
                    if (l == l2) {
                        common.add(l);
                    }
                }
            }
            for (Integer l : common) {
                commonUser.add(storage.get(l));
            }
        }
        log.info("Получен список друзей, общих с другим пользователем.");
        return commonUser;
    }

    @Override
    public void addFriends(Integer id, Integer friendId) {
        if (storage.containsKey(friendId)) {
            storage.get(id).get().addFriends(friendId);
            storage.get(friendId).get().addFriends(id);
            log.info("Пользователь добавлен в друзья.");
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }

    }

    //Удалить пользователя из друзей
    public void deleteFriend(Integer id, Integer fid) {
        if (storage.containsKey(id)) {
            storage.get(id).get().getFriends().remove(fid);
            log.info("Пользователь удален из друзей.");
        } else {
            log.info("Друган не найден.");
            throw new NotFoundException("Друган не найден.");
        }
    }
}
