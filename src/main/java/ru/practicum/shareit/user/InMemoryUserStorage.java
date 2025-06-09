package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ExistingEmailsException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> idToUser = new HashMap<>();
    private final Set<String> existingEmails = new HashSet<>();
    private Long idCounter = 1L;

    @Override
    public User getById(Long id) {
        return idToUser.values()
                .stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = String.format("Пользователь с id %d не найден", id);
                    throw new UserNotFoundException(errorMessage);
                });
    }

    @Override
    public User create(User user) {
        if (existingEmails.contains(user.getEmail())) {
            throw new ExistingEmailsException("Пользователь с email " + user.getEmail() + " уже существует");
        }
        user.setId(idCounter++);
        idToUser.put(user.getId(), user);
        existingEmails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (!idToUser.containsKey(id)) {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            throw new UserNotFoundException(errorMessage);
        }
        User existingUser = idToUser.get(id);
        String newEmail = user.getEmail();
        String oldEmail = existingUser.getEmail();


        if (!newEmail.equals(oldEmail) && existingEmails.contains(newEmail)) {
            throw new ExistingEmailsException("Пользователь с email " + user.getEmail() + " уже существует");
        }
        if (!newEmail.equals(oldEmail)) {
            existingEmails.remove(oldEmail);
            existingEmails.add(newEmail);
        }

        idToUser.put(id, user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(idToUser.values());
    }

    @Override
    public void delete(Long id) {
        if (!idToUser.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        User user = idToUser.get(id);
        existingEmails.remove(user.getEmail());
        idToUser.remove(id);
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException {
        return idToUser.values().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
    }

}