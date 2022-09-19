package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private Map<Long, User> storage = new HashMap<>();
    private Long identifier = 0L;


    //Показать всех пользователей
    public Map<Long, User> getStorage() {
        return storage;
    }


    //Генерация ID номера
    public Long generationId() {
        identifier += 1;
        return identifier;
    }

    //Создание пользователя
    public User create(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        user.setId(generationId());
        getStorage().put(user.getId(), user);
        log.info("Создан пользователь.");
        return user;
    }

    //Обновление пользователя
    public User update(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        if (storage.containsKey(user.getId())) {
            storage.put(user.getId(), user);
            log.info("Пользователь обновлен.");
            return user;
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    //Удаление пользователя
    public void delete(Long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            log.info("Пользователь удален.");
        } else {
            log.info("Пользователь не найден.");
            throw new ValidationException("Такого пользователя нет.");
        }
    }

    //показать пользователя
    public User getUser(Long id) {
        if (storage.containsKey(id)) {
            log.info("Пользователь получен.");
            return storage.get(id);
        } else {
            log.info("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>(storage.values());
        log.info("Получен список пользователей.");
        return users;
    }
}
