package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    public Map<Long, User> getStorage();
    public User create(User user);
    public User update(User user);
    public void delete(Long id);
    public User getUser(Long id);
    public List<User> getUsers();
}
