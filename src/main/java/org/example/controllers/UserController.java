package org.example.controllers;

import org.example.dto.UserDto;
import org.example.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserFields(@PathVariable Long userId, @RequestBody UserDto user) {
        user.setId(userId);
        userService.updateUser(user);
        return ResponseEntity
                .ok("User fields updated successfully.");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> partialUpdateUser(@PathVariable Long userId, @RequestBody UserDto user) {
        user.setId(userId);
        userService.updateUser(user);
        return ResponseEntity
                .ok("User fields updated successfully.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .ok()
                .body("User deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByBirthDateRange(Pageable pageable, @RequestParam("from") String from,
                                                         @RequestParam("to") String to) {
        Page<UserDto> matchedUsers = userService.findUsersByBirthDateRange(from, to, pageable);
        return ResponseEntity
                .ok(matchedUsers);
    }
}
