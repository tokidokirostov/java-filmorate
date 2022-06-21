package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    //public Map<Integer, Optional<User>> getStorage();
    public User create(User user);
    public User update(User user);
    public Optional<User> getUser(Long id);
    public List<Optional<User>> getUsers();

    public List<Optional<User>> findAllUserFriends(Long userId);
    public List<Optional<User>> commonFriends(Long id, Long otherId);
    public void addFriends(Long id, Long friendId);
    public void deleteFriend(Long id, Long fid);
}
