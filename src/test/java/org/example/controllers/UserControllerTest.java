package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.TestDataUtil;
import org.example.dto.UserDto;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        UserDto userDto = TestDataUtil.createTestUserA();

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        String jsonString = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    public void testUpdateUserFields() throws Exception {
        UserDto userDto = TestDataUtil.createTestUserA();
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        Mockito.verify(userService).updateUser(userDto);
    }

    @Test
    public void testPartialUpdateUser() throws Exception {
        UserDto userDto = TestDataUtil.createTestUserA();
        userDto.setEmail("jone-doe@gmail.com");
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        Mockito.verify(userService).updateUser(userDto);
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;

        when(userService.deleteUser(userId)).thenReturn(Optional.of(TestDataUtil.createTestUserA()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User deleted successfully"));
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        String fromDate = "2024-01-01";
        String toDate = "2024-12-31";

        UserDto userDto = TestDataUtil.createTestUserA();
        PageImpl<UserDto> page = new PageImpl<>(Collections.singletonList(userDto));

        when(userService.findUsersByBirthDateRange(eq(fromDate), eq(toDate), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("from", fromDate)
                        .param("to", toDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value(userDto.getEmail()));
    }
}
