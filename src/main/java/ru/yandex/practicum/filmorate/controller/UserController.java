package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int identifier = 0;
    // создаём логер
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/users")
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Получен запрос POST /users");

        boolean dd = users.values().stream()
                .anyMatch(user1 -> user1.getLogin().equals(user.getLogin()));

        boolean spaceWhithLogin = StringUtils.containsWhitespace(user.getLogin());
        boolean dogFromEmail = user.getEmail().contains("@");
        if (user.getLogin().equals("")) {
            log.info("Создание нового пользователя. Невыполнено. Отсутствует Login у пользователя.");
            throw new ValidationException("Отсутствует Login у пользователя.");
        }
        if (spaceWhithLogin) {
            log.info("Создание нового пользователя. Невыполнено. В Login есть пробелы.");
            throw new ValidationException("В Login есть пробелы.");
        }
        if (user.getEmail().equals("")) {
            log.info("Создание нового пользователя. Невыполнено. Email отсутствует.");
            throw new ValidationException("Email отсутствует.");
        }
        if (!dogFromEmail) {
            log.info("Создание нового пользователя. Невыполнено. В Email отсутствует символ @.");
            throw new ValidationException("В Email отсутствует символ @.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Создание нового пользователя. Невыполнено. День рождения пользователя в будующем.");
            throw new ValidationException("День рождения пользователя в будующем.");
        }
        if (dd) {
            log.info("Создание нового пользователя. Невыполнено. Пользователь уже существует.");
            throw new ValidationException("Пользователь уже существует.");
        }

        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        identifier++;
        user.setId(identifier);
        users.put(identifier, user);
        log.info("Создание нового пользователя. Выполнено.");

        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("Получен запрос PUT /users");
        boolean spaceWhithLogin = StringUtils.containsWhitespace(user.getLogin());
        boolean dogFromEmail = user.getEmail().contains("@");
        if (user.getLogin().equals("")) {
            log.info("Обновление пользователя. Невыполнено. Отсутствует Login у пользователя.");
            throw new ValidationException("Отсутствует Login у пользователя.");
        }
        if (spaceWhithLogin) {
            log.info("Обновление пользователя. Невыполнено. В Login есть пробелы.");
            throw new ValidationException("В Login есть пробелы.");
        }
        if (user.getEmail().equals("")) {
            log.info("Обновление пользователя. Невыполнено. Email отсутствует.");
            throw new ValidationException("Email отсутствует." + dogFromEmail);
        }
        if (!dogFromEmail) {
            log.info("Обновление пользователя. Невыполнено. В Email отсутствует символ @.");
            throw new ValidationException("В Email отсутствует символ @.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Обновление пользователя. Невыполнено. День рождения пользователя в будующем.");
            throw new ValidationException("День рождения пользователя в будующем.");
        }
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя заменено на Login.");
        }
        users.put(user.getId(), user);
        log.info("Обновление пользователя. Выполнено.");

        return user;

    }
}
