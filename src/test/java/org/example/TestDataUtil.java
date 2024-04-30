package org.example;

import org.example.dto.UserDto;

import java.time.LocalDate;

public final class TestDataUtil {
    public static UserDto createTestUserA() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail("john@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("123-456-7890");

        return user;
    }

    public static UserDto createTestUserB() {
        UserDto user = new UserDto();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1995, 5, 15));

        return user;
    }
}
