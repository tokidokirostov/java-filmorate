package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {
    //private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        //this.userStorage = userStorage;
        this.userService = userService;
    }

    //Получение всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос GET");
        return userService.getUserStorage().getStorage().values();
    }

    //Создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST");
        return userService.getUserStorage().create(user);
    }

    //Обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT");
        return userService.getUserStorage().update(user);
    }

    //Удаление пользователя
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос DELETE /{}. удаление пользователя.", id);
        userService.getUserStorage().delete(id);
    }

    //Показать пользователя
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Получен запрос GET /users/{}", id);
        return userService.getUserStorage().getUser(id);
    }

    //Добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен запрос PUT /users/{}/friends/{}", id, friendId);
        userService.addFriends(id, friendId);
    }

    //Показать друзей пользователя
    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        log.info("Получен запрос GET /users/{}/friends", id);
        return userService.findAllUsers(id);
    }

    //Удаление друга
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFiends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен запрос DELETE /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonUser(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен запрос GET /users/{}/friends/common/{}", id, otherId);
        return userService.commonFriends(id, otherId);
    }
}

