package ru.practicum.model.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.user.userDto.NewUserRequest;
import ru.practicum.model.user.userDto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    @Transactional
    void deleteUser(Long id);
}
