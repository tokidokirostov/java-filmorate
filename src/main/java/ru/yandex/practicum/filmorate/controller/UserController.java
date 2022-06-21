package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    //Получение всех пользователей+
    @GetMapping
    public Collection<Optional<User>> findAll() {
        log.info("Получен запрос GET");
        return userService.getUsers();
    }

    //Создание пользователя+
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST");
        return userService.create(user);
    }

    //Обновление пользователя+
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT");
        return userService.update(user);
    }

    //Показать пользователя+
    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Integer id) {
        log.info("Получен запрос GET /users/{}", id);
        return userService.getUser(id);
    }

    //Добавление в друзья+
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос PUT /users/{}/friends/{}", id, friendId);
        userService.addFriends(id, friendId);
    }

    //Показать друзей пользователя+
    @GetMapping("/{id}/friends")
    public List<Optional<User>> getAllFriends(@PathVariable Integer id) {
        log.info("Получен запрос GET /users/{}/friends", id);
        return userService.findAllUserFriends(id);
    }

    //Удаление друга+
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFiends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос DELETE /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    //список друзей, общих с другим пользователем+
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<Optional<User>> getCommonUser(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен запрос GET /users/{}/friends/common/{}", id, otherId);
        return userService.commonFriends(id, otherId);
    }
}

