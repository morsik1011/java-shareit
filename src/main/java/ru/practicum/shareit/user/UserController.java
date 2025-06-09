package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", userDto);
        UserDto createdUser = userService.create(userDto);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", userDto);
        return createdUser;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Получен HTTP-запрос на получение пользователя по id: {}", userId);
        UserDto user = userService.getById(userId);
        log.info("Найденный пользователь: {}", user);
        return user;
    }


    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
                              @RequestBody UserDto userDto) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", userDto);
        UserDto updatedUser = userService.update(userId, userDto);
        log.info("Успешно обработан HTTP-запрос на обновление пользователя: {}", userDto);
        return updatedUser;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен HTTP-запрос на удаление пользователя по id: {}", userId);
        userService.delete(userId);
    }
}
