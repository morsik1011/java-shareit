package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ExistingEmailsException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {

        this.userStorage = userStorage;
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User createdUser = userStorage.create(user);
        return UserMapper.toUserDto(createdUser);
    }


    @Override
    public UserDto getById(Long userId) {
        User user = userStorage.getById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User existingUser = userStorage.getById(userId);

        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            try {
                User userWithSameEmail = userStorage.getByEmail(userDto.getEmail());
                if (userWithSameEmail != null && !userWithSameEmail.getId().equals(userId)) {
                    throw new ExistingEmailsException("Пользователь с email " + userDto.getEmail() + " уже существует");
                }
            } catch (UserNotFoundException e) {
                log.debug("Пользователь с email не найден");
            }
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        User updatedUser = userStorage.update(existingUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        userStorage.delete(userId);
    }
}
