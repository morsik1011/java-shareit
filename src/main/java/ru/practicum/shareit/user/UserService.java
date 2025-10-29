package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    UserDto getById(Long userId);

    UserDto update(Long userId, UserDto userDto);

    List<UserDto> getAll();

    void delete(Long userId);
}
