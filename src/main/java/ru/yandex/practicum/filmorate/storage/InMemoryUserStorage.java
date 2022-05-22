package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private Map<Long, User> storage = new HashMap<>();
    private Long identifier = 0L;

    public Map<Long, User> hello() {
        User user1 = new User(1L, "m1@ya.ru", "katya", "Katya", LocalDate.of(1999, 10, 12));
        User user2 = new User(2L, "m2@ya.ru", "katya2", "Katya2", LocalDate.of(1999, 10, 12));
        User user3 = new User(3L, "m3@ya.ru", "katya3", "Katya3", LocalDate.of(1999, 10, 12));
        storage.put(user1.getId(), user1);
        storage.put(user2.getId(), user2);
        storage.put(user3.getId(), user3);

        return storage;
    }

    //Показать всех пользователей
    public Map<Long, User> getStorage() {
        log.info("получен список пользователей.");
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
}
