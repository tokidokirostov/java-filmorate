package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    public Map<Integer, Optional<User>> getStorage();
    public User create(User user);
    public User update(User user);
    public Optional<User> getUser(Integer id);
    public List<Optional<User>> getUsers();

    public List<Optional<User>> findAllUserFriends(Integer userId);
    public List<Optional<User>> commonFriends(Integer id, Integer otherId);
    public void addFriends(Integer id, Integer friendId);
    public void deleteFriend(Integer id, Integer fid);
}
