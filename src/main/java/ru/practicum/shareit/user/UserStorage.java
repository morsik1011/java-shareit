package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User getById(Long id);

    List<User> getAll();

    void delete(Long id);

    User getByEmail(String email);
}
