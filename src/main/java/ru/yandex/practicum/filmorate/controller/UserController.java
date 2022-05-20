package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST /users. Создание пользователя.");

        boolean dd = getStorage().values().stream()
                .anyMatch(user1 -> user1.getLogin().equals(user.getLogin()));

        if (dd) {
            log.info("Создание нового пользователя. Невыполнено. Пользователь уже существует.");
            throw new ValidationException("Пользователь уже существует.");
        }

        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        user.setId(generationId());
        getStorage().put(user.getId(), user);
        log.info("Выполнено.");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT /users. Обновление пользователя.");
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        getStorage().put(user.getId(), user);
        log.info("Выполнено.");
        return user;
    }
}
