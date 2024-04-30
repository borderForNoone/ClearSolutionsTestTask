package org.example.services.impl;

import org.example.domain.UserEntity;
import org.example.dto.UserDto;
import org.example.exceptions.InvalidAgeException;
import org.example.exceptions.InvalidDateException;
import org.example.exceptions.UserNotFoundException;
import org.example.mappers.Mapper;
import org.example.repositories.UserRepository;
import org.example.services.ObjectsValidator;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Value("${user.min.age}")
    private int minAge;
    private final ObjectsValidator<UserDto> userValidator;
    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserServiceImpl(ObjectsValidator<UserDto> userValidator, UserRepository userRepository, Mapper<UserEntity, UserDto> userMapper) {
        this.userValidator = userValidator;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto user) {
        userValidator.validate(user);
        isAdult(user.getBirthDate());
        UserEntity userEntity = userMapper.mapFrom(user);

        return userMapper.mapTo(userRepository.save(userEntity));
    }

    @Override
    public Optional<UserDto> updateUser(UserDto user) {
        userValidator.validate(user);
        isAdult(user.getBirthDate());
        Optional<UserDto> userOptional = findUserById(user.getId());

        UserEntity userEntity = userMapper.mapFrom(userOptional.get());

        userRepository.save(userEntity);

        return Optional.ofNullable(userMapper.mapTo(userEntity));
    }

    @Override
    public Optional<UserDto> deleteUser(Long userId) {
        Optional<UserDto> userOptional = findUserById(userId);

        UserDto userDto = userOptional.get();
        UserEntity userEntity = userMapper.mapFrom(userDto);
        userRepository.delete(userEntity);

        return userOptional;
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<UserDto> findUserById(Long userId) {
        Optional<UserEntity> userOptional = Optional.ofNullable(userRepository.findById(userId));
        if (userOptional.isPresent()) {
            return Optional.ofNullable(userMapper.mapTo(userOptional.get()));
        } else {
            throw new UserNotFoundException("Cannot find user with id: " + userId);
        }
    }

    @Override
    public Page<UserDto> findUsersByBirthDateRange(String from, String to, Pageable pageable) {
        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            if (fromDate.isAfter(toDate)) {
                throw new InvalidDateException("'From' date must be before 'To' date.");
            }

            Page<UserEntity> usersPage = userRepository.findByBirthDateBetween(fromDate, toDate, pageable);
            return usersPage.map(userMapper::mapTo);
        } catch (DateTimeParseException ex) {
            throw new InvalidDateException("Invalid date format. Please provide dates in the format 'yyyy-MM-dd'.");
        }
    }


    private void isAdult(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minAdultDate = birthDate.plusYears(minAge);

        if (currentDate.isBefore(minAdultDate)) {
            throw new InvalidAgeException("Users must be over " + minAge + " years old.");
        }
    }
}
