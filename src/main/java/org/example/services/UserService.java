package org.example.services;

import org.example.domain.UserEntity;
import org.example.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto user);

    Optional<UserDto> updateUser(UserDto user);

    Optional<UserDto> deleteUser(Long userId);

    Page<UserEntity> findAll(Pageable pageable);

    Optional<UserDto> findUserById(Long userId);

    Page<UserDto> findUsersByBirthDateRange(String from, String to, Pageable pageable);
}
