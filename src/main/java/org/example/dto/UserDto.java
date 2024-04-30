package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class UserDto {
    private Long id;

    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    @NotNull
    private String email;

    @NotEmpty(message = "First name should not be empty")
    @NotNull
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @NotNull
    private String lastName;

    @Past(message = "Value must be earlier than current date")
    @NotNull
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
