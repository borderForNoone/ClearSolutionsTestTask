package org.example.services;

import org.example.TestDataUtil;
import org.example.dto.UserDto;
import org.example.exceptions.InvalidAgeException;
import org.example.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserServiceImpl.class)
@AutoConfigureMockMvc
public class UserServiceTest {
    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() {
        UserDto userDto = TestDataUtil.createTestUserA();

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getId(), createdUser.getId());
    }

    @Test
    public void testCreateUserInvalidBirthDate() {
        UserDto user = TestDataUtil.createTestUserA();
        user.setBirthDate(LocalDate.parse("2022-01-01"));

        Mockito.when(userService.createUser(user)).thenThrow(InvalidAgeException.class);

        Assertions.assertThrows(InvalidAgeException.class, () -> userService.createUser(user));
    }

    @Test
    public void testDeleteUser() {
        UserDto userDto = TestDataUtil.createTestUserA();

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        Optional<UserDto> deletedUser = Optional.of(userDto);
        when(userService.deleteUser(userDto.getId())).thenReturn(deletedUser);

        UserDto createdUser = userService.createUser(userDto);
        Optional<UserDto> result = userService.deleteUser(createdUser.getId());

        Assertions.assertTrue(result.isPresent());
        assertEquals(userDto.getId(), result.get().getId());
        assertEquals(userDto.getLastName(), result.get().getLastName());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(userDto.getId());
    }

    @Test
    public void testFindUserById() {
        UserDto userDto = TestDataUtil.createTestUserA();

        Optional<UserDto> foundUser = Optional.of(userDto);
        when(userService.findUserById(userDto.getId())).thenReturn(foundUser);

        Optional<UserDto> result = userService.findUserById(userDto.getId());

        Assertions.assertTrue(result.isPresent());
        assertEquals(userDto.getId(), result.get().getId());
        assertEquals(userDto.getLastName(), result.get().getLastName());

        Mockito.verify(userService, Mockito.times(1)).findUserById(userDto.getId());
    }

    @Test
    public void testUpdateUser() {
        UserDto existingUser = TestDataUtil.createTestUserA();
        existingUser.setFirstName("Updated Name");

        when(userService.findUserById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(existingUser)).thenReturn(Optional.of(existingUser));

        Optional<UserDto> updatedUser = userService.updateUser(existingUser);

        Assertions.assertTrue(updatedUser.isPresent());
        assertEquals(existingUser.getId(), updatedUser.get().getId());
        assertEquals("Updated Name", updatedUser.get().getFirstName());
    }

    @Test
    public void testFindUsersByBirthDateRange() {
        String fromDate = "2024-01-01";
        String toDate = "2024-12-31";
        Pageable pageable = PageRequest.of(0, 10);

        UserDto userDto = TestDataUtil.createTestUserA();
        PageImpl<UserDto> page = new PageImpl<>(Collections.singletonList(userDto));

        when(userService.findUsersByBirthDateRange(eq(fromDate), eq(toDate), any(Pageable.class)))
                .thenReturn(page);

        Page<UserDto> result = userService.findUsersByBirthDateRange(fromDate, toDate, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userDto.getId(), result.getContent().get(0).getId());
        assertEquals(userDto.getLastName(), result.getContent().get(0).getLastName());

        verify(userService, times(1)).findUsersByBirthDateRange(fromDate, toDate, pageable);
    }
}
