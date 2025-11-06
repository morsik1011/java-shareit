package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ExistingEmailsException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto create
            (UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User createdUser = userRepository.save(user);
        return UserMapper.toUserDto(createdUser);
    }


    @Override
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException
                        (String.format("Пользователь с ID %d не найден", userId)));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update
            (Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException
                        (String.format("Пользователь с ID %d не найден", userId)));

        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            try {
                User userWithSameEmail = userRepository.findByEmail(userDto.getEmail());
                if (userWithSameEmail != null && !userWithSameEmail.getId().equals(userId)) {
                    throw new ExistingEmailsException(String.format("Пользователь с email %s уже существует", userDto.getEmail()));
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

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с ID %d не найден", userId));
        }
        userRepository.deleteById(userId);
    }
}
