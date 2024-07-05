package ru.practicum.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.exeption.AlreadyExistException;
import ru.practicum.model.user.userDto.NewUserRequest;
import ru.practicum.model.user.userDto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        if (userRepository.findAll().stream()
                .anyMatch(category -> category.getName().equals(newUserRequest.getName()))) {
            throw new AlreadyExistException("Категория с таким именем уже существует");
        }
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.mapToUser(newUserRequest)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (ids == null) {
            return userRepository.findAll(page).stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findByUserIdIn(ids, page).stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
